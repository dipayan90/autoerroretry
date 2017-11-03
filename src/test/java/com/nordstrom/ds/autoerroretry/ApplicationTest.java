package com.nordstrom.ds.autoerroretry;

import com.nordstrom.ds.autoerroretry.api.LoaderHandlerClient;
import com.nordstrom.ds.autoerroretry.converters.ObjectStringConverter;
import com.nordstrom.ds.autoerroretry.model.MessageBroker;
import com.nordstrom.ds.autoerroretry.model.PublishErrorMessageRequest;
import com.nordstrom.ds.autoerroretry.model.ReceiveErrorMessageRequest;
import com.nordstrom.ds.autoerroretry.vo.Transaction;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest {

    @Test
    @Ignore
    public void testCompleteFlowUsingSQS() throws Exception{
        String queueUrl = "https://sqs.******.amazonaws.com/*******/transaction-***";
        List<String> messageList = new ArrayList<>();

        Transaction transaction1 = new Transaction();
        transaction1.setDate("12/27/1990");
        transaction1.setTransactionId(1);
        transaction1.setValue(100);

        Transaction transaction2 = new Transaction();
        transaction2.setDate("10/08/1991");
        transaction2.setTransactionId(2);
        transaction2.setValue(200);

        ObjectStringConverter converter = ObjectStringConverter.getObjectStringConverter();

        messageList.add(converter.toString(transaction1));
        messageList.add(converter.toString(transaction2));

        LoaderHandlerClient client = new LoaderHandlerClient();

        AtomicInteger value = new AtomicInteger(0);

        client.publishRetries(new PublishErrorMessageRequest.
                PublishErrorMessageRequestBuilder()
                .withMessages(messageList)
                .withMessageBroker(MessageBroker.SQS)
                .withSqsUrl(queueUrl).build());

        client.receiveRetires(new ReceiveErrorMessageRequest
                .ReceiveErrorMessageRequestBuilder()
                .withMessageBroker(MessageBroker.SQS)
                .withSqsUrl(queueUrl)
                .withPingInterval(5)
                .build(), strings -> {
            strings.forEach(e -> { value.addAndGet(1);
                try {
                    System.out.println( "  Message:  " + converter.fromString(e).toString());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });
            return null;
        });

        Thread.sleep(100000);
        Assert.assertTrue(value.get() > 0);
    }

    @Test
    @Ignore
    public void testCompleteFlowUsingKafka() throws Exception{
        List<String> messageList = new ArrayList<>();

        Transaction transaction1 = new Transaction();
        transaction1.setDate("12/27/1990");
        transaction1.setTransactionId(1);
        transaction1.setValue(100);

        Transaction transaction2 = new Transaction();
        transaction2.setDate("10/08/1991");
        transaction2.setTransactionId(2);
        transaction2.setValue(200);

        ObjectStringConverter converter = ObjectStringConverter.getObjectStringConverter();

        messageList.add(converter.toString(transaction1));
        messageList.add(converter.toString(transaction2));

        LoaderHandlerClient client = new LoaderHandlerClient();

        AtomicInteger value = new AtomicInteger(0);

        client.publishRetries(new PublishErrorMessageRequest.
                PublishErrorMessageRequestBuilder()
                .withMessages(messageList)
                .withMessageBroker(MessageBroker.KAFKA)
                .withKafkaRetries(0)
                .withKafkaServers(Collections.singletonList("localhost:9092"))
                .withKafkaTopic("retry")
                .withOrderGuarentee(true)
                .build());

        client.receiveRetires(new ReceiveErrorMessageRequest
                .ReceiveErrorMessageRequestBuilder()
                .withMessageBroker(MessageBroker.KAFKA)
                .withKafkaServers(Collections.singletonList("localhost:9092"))
                .withKafkaConsumerGroupId("localConsumer")
                .withKafkaTopicName("retry")
                .withPingInterval(5)
                .build(), strings -> {
            strings.forEach(e -> { value.addAndGet(1);
                try {
                    System.out.println( "  Message:  " + converter.fromString(e).toString());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });
            return null;
        });

        Thread.sleep(100000);
        Assert.assertTrue(value.get() > 0);
    }

    @Test
    public void testCompleteFlowUsingTape() throws Exception{
        List<String> messageList = new ArrayList<>();
        String tapeFileName = new File("").getAbsolutePath() + "/src/test/resources/tape.txt";
        Transaction transaction1 = new Transaction();
        transaction1.setDate("12/27/1990");
        transaction1.setTransactionId(1);
        transaction1.setValue(100);

        Transaction transaction2 = new Transaction();
        transaction2.setDate("10/08/1991");
        transaction2.setTransactionId(2);
        transaction2.setValue(200);

        ObjectStringConverter converter = ObjectStringConverter.getObjectStringConverter();

        messageList.add(converter.toString(transaction1));
        messageList.add(converter.toString(transaction2));

        LoaderHandlerClient client = new LoaderHandlerClient();

        AtomicInteger value = new AtomicInteger(0);

        client.publishRetries(new PublishErrorMessageRequest.
                PublishErrorMessageRequestBuilder()
                .withMessages(messageList)
                .withMessageBroker(MessageBroker.TAPE)
                .withTapeFileName(tapeFileName)
                .build());

        client.receiveRetires(new ReceiveErrorMessageRequest
                .ReceiveErrorMessageRequestBuilder()
                .withMessageBroker(MessageBroker.TAPE)
                .withtapeFileName(tapeFileName)
                .withPingInterval(5)
                .build(), strings -> {
            strings.forEach(e -> { value.addAndGet(1);
                try {
                    System.out.println( "  Message:  " + converter.fromString(e).toString());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });
            return null;
        });

        Thread.sleep(1000);
        Assert.assertTrue(value.get() > 0);
    }

}
