package com.nordstrom.ds.autoerroretry.model;

import java.util.List;
import java.util.Properties;

public class KafkaConnectionSettings implements ConnectionSettings {

    private Properties properties;

    /**
     *
     * @param bootstrapServers
     * @param topicName
     * @param retries
     * @param maintainOrder
     */
    public KafkaConnectionSettings(List<String> bootstrapServers, String topicName, int retries, boolean maintainOrder){
        properties = new Properties();
        properties.put("bootstrap.servers",bootstrapServers);
        properties.setProperty("topic",topicName);
        properties.setProperty("acks","all");
        properties.put("retries",retries);
        properties.put("batch.size",16384);
        properties.put("linger.ms",1);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("maintainOrder",maintainOrder);
    }

    /**
     *
     * @param bootstrapServers
     * @param topicName
     * @param consumerGroup
     */
    public KafkaConnectionSettings(List<String> bootstrapServers, String topicName,String consumerGroup){
        properties = new Properties();
        properties.put("bootstrap.servers",bootstrapServers);
        properties.setProperty("topic",topicName);
        properties.put("group.id", consumerGroup);
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    }


    @Override
    public Properties getProperties() {
        return properties;
    }
}
