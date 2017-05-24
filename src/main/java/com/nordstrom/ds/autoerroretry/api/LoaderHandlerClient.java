package com.nordstrom.ds.autoerroretry.api;

import com.amazonaws.services.sqs.model.Message;
import com.nordstrom.ds.autoerroretry.model.PublishErrorMessageRequest;
import com.nordstrom.ds.autoerroretry.model.ReceiveErrorMessageRequest;
import com.nordstrom.ds.autoerroretry.service.Publisher;
import com.nordstrom.ds.autoerroretry.service.Receiver;
import com.nordstrom.ds.autoerroretry.service.sqs.SQSPublisher;
import com.nordstrom.ds.autoerroretry.service.sqs.SQSReceiver;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;


public class LoaderHandlerClient {

    /**
     * Publish error messages specifying the connection settings for queue and the objects you want to persist in the queue
     * @param publishErrorMessageRequest
     * @throws AssertionError
     */
    public void publishErrors(PublishErrorMessageRequest publishErrorMessageRequest) throws AssertionError{
        assert publishErrorMessageRequest!=null;
        assert publishErrorMessageRequest.getSqsUrl()!=null;
        publish(publishErrorMessageRequest.getSqsUrl(),publishErrorMessageRequest.getMessages());
    }

    /**
     * Publish messages to be retired specifying the connection settings for queue and the objects you want to retry
     * @param publishErrorMessageRequest
     * @throws AssertionError
     */
    public void publishRetries(PublishErrorMessageRequest publishErrorMessageRequest) throws AssertionError{
        assert publishErrorMessageRequest!=null;
        assert publishErrorMessageRequest.getSqsUrl()!=null;
        publish(publishErrorMessageRequest.getSqsUrl(),publishErrorMessageRequest.getMessages());
    }

    /**
     * Specify connection settings to the queue, pass the function that you want to be executed along with how often you want the retry logic to work.
     * @param receiveErrorMessageRequest
     * @param function
     * @throws AssertionError
     */
    public void receiveRetires(ReceiveErrorMessageRequest receiveErrorMessageRequest, Function<List<String>,Void> function) throws AssertionError{
        assert receiveErrorMessageRequest!=null;
        assert receiveErrorMessageRequest.getSqsUrl()!=null;
        final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Receiver receiver = SQSReceiver.getReceiver();
                List<Message> objects = receiver.receive(receiveErrorMessageRequest.getSqsUrl());
                function.apply(objects.stream().map(Message::getBody).collect(Collectors.toList()));
                // Once Message is received delete it from the queue
                receiver.deleteMessagesFromQueue(objects,receiveErrorMessageRequest.getSqsUrl());
            }
        }, 0, receiveErrorMessageRequest.getPingInterval() == 0 ? 10: receiveErrorMessageRequest.getPingInterval() , TimeUnit.SECONDS);

    }

    private void publish(String sqsUrl, List<String> messages){
        Publisher publisher = SQSPublisher.getPublisher();
        publisher.publish(sqsUrl,messages);
    }

}
