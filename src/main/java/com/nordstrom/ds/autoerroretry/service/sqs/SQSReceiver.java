package com.nordstrom.ds.autoerroretry.service.sqs;


import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.nordstrom.ds.autoerroretry.config.ApplicationConfig;
import com.nordstrom.ds.autoerroretry.service.Receiver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SQSReceiver implements Receiver{

    private SQSReceiver(){}

    private static SQSReceiver receiver;

    public static SQSReceiver getReceiver(){
        if(receiver == null){
            receiver = new SQSReceiver();
        }
        return receiver;
    }

    private AmazonSQS sqs  = ApplicationConfig.getApplicationConfig().getsqsclient();

    /**
     *  http://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_ReceiveMessage.html
     * This is done because sqs api does a short polling. The only way to receive all messages, is to query sqs multiple times.
     *
     * This Recieves all the messages from the queue and sends it to the client.
     */
    public List<Message> receive(final String sqsUrl){
        assert sqsUrl!=null;
        List<Message> response = new ArrayList<>();
        while(getNumberOfObjectsToRetry(sqsUrl) > 0){
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                    .withQueueUrl(sqsUrl)
                    .withWaitTimeSeconds(10);
            ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(receiveMessageRequest);
            response.addAll(receiveMessageResult.getMessages());
        }
        return response;
    }

    /**
     * Provides count of number of messages that still have to be retried.
     * @param sqsUrl
     * @return
     */
    public Integer getNumberOfObjectsToRetry(final String sqsUrl){
        assert sqsUrl!=null;
        GetQueueAttributesRequest getQueueAttributesRequest = new GetQueueAttributesRequest()
                .withQueueUrl(sqsUrl)
                .withAttributeNames(QueueAttributeName.All);
        return Integer.valueOf(sqs.getQueueAttributes(getQueueAttributesRequest).getAttributes().get("ApproximateNumberOfMessages"));
    }

    /**
     * Deletes messages from the queue that have already been retried.
     * @param messages
     * @param sqsUrl
     */
    public void deleteMessagesFromQueue(List<Message> messages,final String sqsUrl){
        DeleteMessageBatchRequest deleteMessageBatchRequest = new DeleteMessageBatchRequest(sqsUrl)
                .withEntries(messages
                        .stream()
                        .map( msg -> new DeleteMessageParams(msg.getReceiptHandle(),msg.getMessageId()))
                        .collect(Collectors.toList())
                        .stream()
                        .map(param -> new DeleteMessageBatchRequestEntry().withReceiptHandle(param.receiptHandle).withId(param.id))
                        .collect(Collectors.toList()));
        sqs.deleteMessageBatch(deleteMessageBatchRequest);
    }

    // Java 8 doesn't support Tuple Sigh !!
    class DeleteMessageParams implements Serializable {
        DeleteMessageParams(String receiptHandle,String id){
            this.id = id;
            this.receiptHandle = receiptHandle;
        }
        String receiptHandle;
        String id;
    }
}
