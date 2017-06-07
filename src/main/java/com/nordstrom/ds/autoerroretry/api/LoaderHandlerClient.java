package com.nordstrom.ds.autoerroretry.api;

import com.amazonaws.services.sqs.model.Message;
import com.nordstrom.ds.autoerroretry.model.*;
import com.nordstrom.ds.autoerroretry.service.Publisher;
import com.nordstrom.ds.autoerroretry.service.Receiver;
import com.nordstrom.ds.autoerroretry.service.kafka.KafkaPublisher;
import com.nordstrom.ds.autoerroretry.service.kafka.KafkaReceiver;
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
        publish(publishErrorMessageRequest);
    }

    /**
     * Publish messages to be retired specifying the connection settings for queue and the objects you want to retry
     * @param publishErrorMessageRequest
     * @throws AssertionError
     */
    public void publishRetries(PublishErrorMessageRequest publishErrorMessageRequest) throws AssertionError{
        publish(publishErrorMessageRequest);
    }

    /**
     * Specify connection settings to the queue, pass the function that you want to be executed along with how often you want the retry logic to work.
     * @param receiveErrorMessageRequest
     * @param function
     * @throws AssertionError
     */
    public void receiveRetires(ReceiveErrorMessageRequest receiveErrorMessageRequest, Function<List<String>,Void> function) throws AssertionError{
        assert receiveErrorMessageRequest!=null;
        assert receiveErrorMessageRequest.getMessageBroker() != null;
        if(receiveErrorMessageRequest.getMessageBroker().equals(MessageBroker.SQS)){
            receiveAndProcessFromSqs(receiveErrorMessageRequest,function);
        }else if(receiveErrorMessageRequest.getMessageBroker().equals(MessageBroker.KAFKA)){
            receiveAndProcessFromKafka(receiveErrorMessageRequest,function);
        }
    }

    /**
     * Provides the count of number of objects that have to be retried.
     * @param sqsUrl
     * @return
     */
    public Integer getNumberOfObjectsToRetry(final String sqsUrl){
        return SQSReceiver.getReceiver().getNumberOfObjectsToRetry(sqsUrl);
    }

    private void publish(PublishErrorMessageRequest publishErrorMessageRequest){
        assert publishErrorMessageRequest!=null;
        assert publishErrorMessageRequest.getMessageBroker() != null;
        if(publishErrorMessageRequest.getMessageBroker().equals(MessageBroker.SQS)){
            assert publishErrorMessageRequest.getSqsUrl()!=null;
            publishToSqs(publishErrorMessageRequest.getSqsUrl(),publishErrorMessageRequest.getMessages());
        }else if(publishErrorMessageRequest.getMessageBroker().equals(MessageBroker.KAFKA)){
            assert publishErrorMessageRequest.getKafkaServers() != null;
            assert publishErrorMessageRequest.getKafkaTopic() != null;
            publishToKafka(publishErrorMessageRequest.getKafkaServers(),publishErrorMessageRequest.getKafkaTopic(),
                    publishErrorMessageRequest.getKafkaRetries(),publishErrorMessageRequest.getMaintainOrder(),publishErrorMessageRequest.getMessages());
        }
    }

    private void publishToSqs(String sqsUrl, List<String> messages){
        Publisher publisher = SQSPublisher.getPublisher();
        publisher.publish(new SqsConnectionSettings(sqsUrl),messages);
    }

    private void publishToKafka(List<String> kafkaBrokers,String topic,int retires,boolean orderGuarentee,List<String> messages){
        Publisher publisher = KafkaPublisher.getPublisher();
        publisher.publish(new KafkaConnectionSettings(kafkaBrokers,topic,retires,orderGuarentee),messages);
    }



    private void receiveAndProcessFromSqs(ReceiveErrorMessageRequest receiveErrorMessageRequest, Function<List<String>,Void> function){
        assert receiveErrorMessageRequest.getSqsUrl()!=null;
        final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            Receiver receiver = SQSReceiver.getReceiver();
            ReceivedMessage receivedObjects = receiver.receive(new SqsConnectionSettings(receiveErrorMessageRequest.getSqsUrl()));
            function.apply(receivedObjects.getMessageBody());
            // Once Message is received delete it from the queue
            receiver.deleteMessagesFromQueue( receivedObjects.getMessageWrapper().stream().map(obj -> (Message) obj).collect(Collectors.toList()),new SqsConnectionSettings(receiveErrorMessageRequest.getSqsUrl()));
        }, 0, receiveErrorMessageRequest.getPingInterval() == 0 ? 10: receiveErrorMessageRequest.getPingInterval() , TimeUnit.SECONDS);
    }

    private void receiveAndProcessFromKafka(ReceiveErrorMessageRequest receiveErrorMessageRequest, Function<List<String>,Void> function){
        assert receiveErrorMessageRequest.getKafkaServers()!=null;
        assert receiveErrorMessageRequest.getKafkaTopicName()!=null;
        assert receiveErrorMessageRequest.getKafkaConsumerGroupName()!=null;
        final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            Receiver receiver = KafkaReceiver.getReceiver();
            ReceivedMessage receivedObjects = receiver.receive(new KafkaConnectionSettings(receiveErrorMessageRequest.getKafkaServers(),
                    receiveErrorMessageRequest.getKafkaTopicName(),receiveErrorMessageRequest.getKafkaConsumerGroupName()));
            function.apply(receivedObjects.getMessageBody());
        }, 0, receiveErrorMessageRequest.getPingInterval() == 0 ? 10: receiveErrorMessageRequest.getPingInterval() , TimeUnit.SECONDS);
    }


}
