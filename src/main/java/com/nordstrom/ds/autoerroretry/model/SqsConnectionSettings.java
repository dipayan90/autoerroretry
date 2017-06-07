package com.nordstrom.ds.autoerroretry.model;

import java.util.Properties;

public class SqsConnectionSettings implements ConnectionSettings{

    private Properties properties;

    /**
     *
     * @param sqsUrl
     */
    public SqsConnectionSettings(String sqsUrl){
        properties = new Properties();
        this.properties.setProperty("sqsUrl",sqsUrl);
    }

    @Override
    public Properties getProperties() {
        return properties;
    }
}
