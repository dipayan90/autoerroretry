package com.nordstrom.ds.autoerroretry.model;


import java.util.List;

public class PublishErrorMessageRequest {

    /**
     * url for the sqs queue that you have created on aws
     */
    private final String sqsUrl;
    /**
     * All the objects that you want to retry
     */
    private final List<String> messages;
    /**
     * Message broker that you want to use for retries
     */
    private final MessageBroker messageBroker;

    /**
     * Kafka bootstrap server addresses
     */
    private final List<String> kafkaServers;

    /**
     * kafka topic to publish the objects to
     */
    private String kafkaTopic;

    /**
     * How many retires are needed to publish to kafka
     */
    private int kafkaRetries;

    /**
     * Indicate if you want to maintain order for the objects in the kafka messaging system
     */
    private boolean maintainOrder;

    public String getSqsUrl() {
        return sqsUrl;
    }

    public List<String> getMessages() {
        return messages;
    }

    public MessageBroker getMessageBroker(){return messageBroker;}

    public List<String> getKafkaServers(){return kafkaServers;}

    public String getKafkaTopic(){return kafkaTopic;}

    public int getKafkaRetries(){return kafkaRetries;}

    public boolean getMaintainOrder(){return maintainOrder;}

    private PublishErrorMessageRequest(PublishErrorMessageRequestBuilder builder){
        this.sqsUrl = builder.nestedSqsUrl;
        this.messages = builder.nestedMessages;
        this.messageBroker = builder.messageBroker;
        this.kafkaServers = builder.kafkaServers;
        this.kafkaTopic = builder.kafkaTopic;
        this.kafkaRetries = builder.kafkaRetires;
        this.maintainOrder = builder.maintainOrder;
    }

    public static class PublishErrorMessageRequestBuilder{
        private  String nestedSqsUrl;
        private  List<String> nestedMessages;
        private MessageBroker messageBroker;
        private List<String> kafkaServers;
        private String kafkaTopic;
        private int kafkaRetires;
        private boolean maintainOrder;

        public PublishErrorMessageRequestBuilder(){

        }

        public PublishErrorMessageRequestBuilder withSqsUrl(String sqsUrl){
            this.nestedSqsUrl = sqsUrl;
            return this;
        }

        public PublishErrorMessageRequestBuilder withMessages(List<String> messages){
            this.nestedMessages = messages;
            return this;
        }

        public PublishErrorMessageRequestBuilder withMessageBroker(MessageBroker messageBroker){
            this.messageBroker = messageBroker;
            return this;
        }

        public PublishErrorMessageRequestBuilder withKafkaServers(List<String> kafkaServers){
            this.kafkaServers = kafkaServers;
            return this;
        }

        public PublishErrorMessageRequestBuilder withKafkaTopic(String kafkaTopic){
            this.kafkaTopic = kafkaTopic;
            return this;
        }

        public PublishErrorMessageRequestBuilder withKafkaRetries(int kafkaRetires){
            this.kafkaRetires = kafkaRetires;
            return this;
        }

        public PublishErrorMessageRequestBuilder withOrderGuarentee(boolean maintainOrder){
            this.maintainOrder = maintainOrder;
            return this;
        }

        public PublishErrorMessageRequest build(){
            return new PublishErrorMessageRequest(this);
        }
    }
}
