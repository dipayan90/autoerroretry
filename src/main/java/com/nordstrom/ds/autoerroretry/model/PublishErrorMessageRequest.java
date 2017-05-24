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

    public String getSqsUrl() {
        return sqsUrl;
    }

    public List<String> getMessages() {
        return messages;
    }

    private PublishErrorMessageRequest(PublishErrorMessageRequestBuilder builder){
        this.sqsUrl = builder.nestedSqsUrl;
        this.messages = builder.nestedMessages;
    }

    public static class PublishErrorMessageRequestBuilder{
        private  String nestedSqsUrl;
        private  List<String> nestedMessages;

        public PublishErrorMessageRequestBuilder(){

        }

        public PublishErrorMessageRequestBuilder(String url,List<String> messages){
            this.nestedSqsUrl = url;
            this.nestedMessages = messages;
        }

        public PublishErrorMessageRequestBuilder withSqsUrl(String sqsUrl){
            this.nestedSqsUrl = sqsUrl;
            return this;
        }

        public PublishErrorMessageRequestBuilder withMessages(List<String> messages){
            this.nestedMessages = messages;
            return this;
        }

        public PublishErrorMessageRequest build(){
            return new PublishErrorMessageRequest(this);
        }
    }
}
