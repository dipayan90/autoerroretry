package com.nordstrom.ds.autoerroretry.service.kafka;


import com.nordstrom.ds.autoerroretry.model.ConnectionSettings;
import com.nordstrom.ds.autoerroretry.service.Publisher;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.List;
import java.util.Properties;

public class KafkaPublisher implements Publisher{

    private KafkaPublisher(){}

    private static KafkaPublisher kafkaPublisher;

    public static KafkaPublisher getPublisher(){
        if(kafkaPublisher == null){
            kafkaPublisher = new KafkaPublisher();
        }
        return kafkaPublisher;
    }

    @Override
    public void publish(ConnectionSettings connectionSettings, List<String> messages) {
        assert connectionSettings!= null;
        assert connectionSettings.getProperties() != null;
        Properties props = connectionSettings.getProperties();
        assert props.get("bootstrap.servers")!= null;
        assert props.getProperty("topic") != null;
        String topic = props.getProperty("topic");
        Producer<String, String> producer = new KafkaProducer<>(props);
        boolean maintainOrder = (boolean) props.get("maintainOrder");
        if(maintainOrder){
            messages.forEach(message -> {
                producer.send(new ProducerRecord<String, String>(topic,
                        "retries", message));
            });
        } else {
            messages.forEach(message -> {
                producer.send(new ProducerRecord<String, String>(topic,
                        Integer.toString(message.hashCode()), message));
            });
        }
        producer.close();
    }
}
