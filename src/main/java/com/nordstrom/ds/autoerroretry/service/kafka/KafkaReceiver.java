package com.nordstrom.ds.autoerroretry.service.kafka;


import com.amazonaws.services.sqs.model.Message;
import com.nordstrom.ds.autoerroretry.model.ConnectionSettings;
import com.nordstrom.ds.autoerroretry.model.ReceivedMessage;
import com.nordstrom.ds.autoerroretry.service.Receiver;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class KafkaReceiver implements Receiver{

    private KafkaReceiver(){}

    private static KafkaReceiver kafkaReceiver;

    public static KafkaReceiver getReceiver(){
        if(kafkaReceiver == null){
            kafkaReceiver = new KafkaReceiver();
        }
        return kafkaReceiver;
    }

    @Override
    public ReceivedMessage receive(ConnectionSettings connectionSettings) {
        assert connectionSettings!= null;
        assert connectionSettings.getProperties() != null;
        Properties props = connectionSettings.getProperties();
        assert props.get("bootstrap.servers")!= null;
        assert props.getProperty("topic") != null;
        String topic = props.getProperty("topic");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singleton(topic));
        List<String> messageBody = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        try{
            while ((System.currentTimeMillis()-startTime)<10000){
                ConsumerRecords<String, String> records = consumer.poll(100);
                records.iterator().forEachRemaining(rec -> {
                    messageBody.add(rec.value());
                });
            }
        }finally {
            consumer.close();
        }
        return new ReceivedMessage(messageBody);
    }

}
