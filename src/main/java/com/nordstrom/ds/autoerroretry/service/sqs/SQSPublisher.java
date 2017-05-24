package com.nordstrom.ds.autoerroretry.service.sqs;


import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.nordstrom.ds.autoerroretry.config.ApplicationConfig;
import com.nordstrom.ds.autoerroretry.service.Publisher;

import java.util.List;
import java.util.stream.Collectors;

public class SQSPublisher implements Publisher{

    private SQSPublisher(){}

    private static SQSPublisher publisher;

    public static SQSPublisher getPublisher(){
        if(publisher == null){
            publisher = new SQSPublisher();
        }
        return publisher;
    }

    /**
     * Publishes the objects sent to a queue for retrying at a later period of time.
     * @param sqsUrl
     * @param messages
     * @throws AssertionError
     */
    public void publish(final String sqsUrl, final List<String> messages) throws AssertionError{
        AmazonSQS sqs  = ApplicationConfig.getApplicationConfig().getsqsclient();
        assert sqsUrl!=null;
        SendMessageBatchRequest send_batch_request = new SendMessageBatchRequest()
                .withQueueUrl(sqsUrl);

        messages.forEach(message -> {
            send_batch_request.setEntries(messages.stream().map(e -> new SendMessageBatchRequestEntry(Integer.toString(e.hashCode()),e)).collect(Collectors.toList()));
        });

        sqs.sendMessageBatch(send_batch_request);
    }

}
