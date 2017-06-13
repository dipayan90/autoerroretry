package com.nordstrom.ds.autoerroretry.model;

import java.util.List;

public class ReceiveErrorMessageRequest {

    /**
     * Url for the sqs queue that you have created on aws
     */
    private final String sqsUrl;

    /**
     * Value in seconds for the interval after which you want to keep pining the queue for new retryable objects.
     */
    private final long pingInterval;
    /**
     * Message broker that you want to use for retries
     */
    private final MessageBroker messageBroker;

    /**
     * Kafka bootstrap server addresses
     */
    private final List<String> kafkaServers;
    /**
     * Topic name to receive the errors
     */
    private String kafkaTopicName;

    /**
     * Group name for the kafka consumers
     */
    private String kafkaConsumerGroupName;

    /**
     * Name of the file for tape
     */
    private String tapeFileName;

    public String getSqsUrl() {
        return sqsUrl;
    }

    public long getPingInterval() {
        return pingInterval;
    }

    public MessageBroker getMessageBroker(){return messageBroker;}

    public List<String> getKafkaServers(){return kafkaServers;}

    public String getKafkaTopicName(){return kafkaTopicName;}

    public String getKafkaConsumerGroupName(){return kafkaConsumerGroupName;}

    public String getTapeFileName() {
        return tapeFileName;
    }

    private ReceiveErrorMessageRequest(ReceiveErrorMessageRequestBuilder builder){
        this.sqsUrl = builder.nestedSqsUrl;
        this.pingInterval = builder.nestedInterval;
        this.messageBroker = builder.messageBroker;
        this.kafkaServers = builder.kafkaServers;
        this.kafkaTopicName = builder.kafkaTopicName;
        this.kafkaConsumerGroupName = builder.kafkaConsumerGroupName;
        this.tapeFileName = builder.tapeFileName;
    }

    public static class ReceiveErrorMessageRequestBuilder{
        private String nestedSqsUrl;
        private long nestedInterval;
        private MessageBroker messageBroker;
        private List<String> kafkaServers;
        private String kafkaTopicName;
        private String kafkaConsumerGroupName;
        private String tapeFileName;


        public ReceiveErrorMessageRequestBuilder(){}

        public ReceiveErrorMessageRequestBuilder(String url,long interval,MessageBroker messageBroker){
            this.nestedInterval = interval;
            this.nestedSqsUrl = url;
            this.messageBroker = messageBroker;
        }

        public ReceiveErrorMessageRequestBuilder withSqsUrl(String sqsUrl){
            this.nestedSqsUrl = sqsUrl;
            return this;
        }

        public ReceiveErrorMessageRequestBuilder withPingInterval(long timeUnit){
            this.nestedInterval = timeUnit;
            return this;
        }

        public ReceiveErrorMessageRequest.ReceiveErrorMessageRequestBuilder withMessageBroker(MessageBroker messageBroker){
            this.messageBroker = messageBroker;
            return this;
        }

        public ReceiveErrorMessageRequest.ReceiveErrorMessageRequestBuilder withKafkaServers(List<String> kafkaServers){
            this.kafkaServers = kafkaServers;
            return this;
        }

        public ReceiveErrorMessageRequest.ReceiveErrorMessageRequestBuilder withKafkaTopicName(String kafkaTopicName){
            this.kafkaTopicName = kafkaTopicName;
            return this;
        }

        public ReceiveErrorMessageRequest.ReceiveErrorMessageRequestBuilder withKafkaConsumerGroupId(String kafkaConsumerGroupId){
            this.kafkaConsumerGroupName = kafkaConsumerGroupId;
            return this;
        }

        public ReceiveErrorMessageRequest.ReceiveErrorMessageRequestBuilder withtapeFileName(String fileName){
            this.tapeFileName = fileName;
            return this;
        }

        public ReceiveErrorMessageRequest build(){
            return new ReceiveErrorMessageRequest(this);
        }

    }

}
