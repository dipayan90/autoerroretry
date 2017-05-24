package com.nordstrom.ds.autoerroretry.model;

public class ReceiveErrorMessageRequest {

    /**
     * Url for the sqs queue that you have created on aws
     */
    private final String sqsUrl;

    /**
     * Value in seconds for the interval after which you want to keep pining the queue for new retryable objects.
     */
    private final long pingInterval;

    public String getSqsUrl() {
        return sqsUrl;
    }

    public long getPingInterval() {
        return pingInterval;
    }

    private ReceiveErrorMessageRequest(ReceiveErrorMessageRequestBuilder builder){
        this.sqsUrl = builder.nestedSqsUrl;
        this.pingInterval = builder.nestedInterval;
    }

    public static class ReceiveErrorMessageRequestBuilder{
        private String nestedSqsUrl;
        private long nestedInterval;

        public ReceiveErrorMessageRequestBuilder(){}

        public ReceiveErrorMessageRequestBuilder(String url,long interval){
            this.nestedInterval = interval;
            this.nestedSqsUrl = url;
        }

        public ReceiveErrorMessageRequestBuilder withSqsUrl(String sqsUrl){
            this.nestedSqsUrl = sqsUrl;
            return this;
        }

        public ReceiveErrorMessageRequestBuilder withPingInterval(long timeUnit){
            this.nestedInterval = timeUnit;
            return this;
        }

        public ReceiveErrorMessageRequest build(){
            return new ReceiveErrorMessageRequest(this);
        }

    }

}
