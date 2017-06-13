package com.nordstrom.ds.autoerroretry.model;


import java.util.Properties;

public class TapeConnectionSettings implements ConnectionSettings{

    private Properties properties;

    public TapeConnectionSettings(String fileName){
        properties = new Properties();
        properties.setProperty("filename",fileName);
    }

    @Override
    public Properties getProperties() {
        return properties;
    }
}
