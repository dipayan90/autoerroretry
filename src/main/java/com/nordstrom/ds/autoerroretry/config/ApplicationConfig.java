package com.nordstrom.ds.autoerroretry.config;


import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQSClient;

public class ApplicationConfig {

    private ApplicationConfig(){}

    private static ApplicationConfig applicationConfig;

    private final String PROD = "prod";
    private final String NONPROD = "non-prod";
    private String sqsUrl;

    private ClientConfiguration getClientConfiguration(){
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setProtocol(Protocol.HTTPS);
        clientConfiguration.setProxyHost("webproxysea.nordstrom.net");
        clientConfiguration.setProxyPort(8181);
        return clientConfiguration;
    }

    public AmazonSQSClient getsqsclient(){
        return new AmazonSQSClient(getAwsCredentialsProvider(), getClientConfiguration());
    }

    public static ApplicationConfig getApplicationConfig(){
        if(applicationConfig == null){
            applicationConfig = new ApplicationConfig();
        }
        return applicationConfig;
    }

    private boolean isEmpty(Object str){
        return (str == null || "".equals(str));
    }

    private boolean isDeployedOnAWS(){
        String environment = System.getProperty("env.class");
        return !isEmpty(environment) && ( environment.equals(PROD) || environment.equals(NONPROD) );
    }

    private AWSCredentialsProvider getAwsCredentialsProvider(){
        if(isDeployedOnAWS()) return new CustomAWSInstanceCredentialsProviderChain(); else return new CustomCredentialsProviderChain();
    }

    public String getSqsUrl() {
        return sqsUrl;
    }

    public void setSqsUrl(String sqsUrl) {
        this.sqsUrl = sqsUrl;
    }

}
