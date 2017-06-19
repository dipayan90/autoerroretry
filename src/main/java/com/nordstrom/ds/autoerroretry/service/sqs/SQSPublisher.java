package com.nordstrom.ds.autoerroretry.service.sqs;


import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.nordstrom.ds.autoerroretry.config.sqs.ApplicationConfig;
import com.nordstrom.ds.autoerroretry.model.ConnectionSettings;
import com.nordstrom.ds.autoerroretry.service.Publisher;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class SQSPublisher implements Publisher{

    private SQSPublisher(){}

    private static SQSPublisher publisher;

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    public static SQSPublisher getPublisher(){
        if(publisher == null){
            publisher = new SQSPublisher();
        }
        return publisher;
    }

    /**
     *
     * @param connectionSettings
     * @param messages
     */
    public void publish(final ConnectionSettings connectionSettings, final List<String> messages)  {
        assert connectionSettings != null;
        assert connectionSettings.getProperties().getProperty("sqsUrl") != null;
        String sqsUrl = connectionSettings.getProperties().getProperty("sqsUrl");
        assert messages != null;
        if (messages.size() != 0) {
            AmazonSQS sqs = ApplicationConfig.getApplicationConfig().getsqsclient();
            SendMessageBatchRequest send_batch_request = new SendMessageBatchRequest()
                    .withQueueUrl(sqsUrl);
            messages.forEach(message -> send_batch_request.setEntries(messages.stream()
                    .map(e -> new SendMessageBatchRequestEntry(Integer.toString(e.hashCode()), e)
                    .withMessageGroupId("retryMessageGroupId")
                    .withMessageDeduplicationId(String.valueOf(message.hashCode()*System.nanoTime())))
                    .collect(Collectors.toList())));
            executor.execute(() -> sqs.sendMessageBatch(send_batch_request));
        }
    }
}
