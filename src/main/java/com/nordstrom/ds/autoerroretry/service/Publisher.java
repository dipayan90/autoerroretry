package com.nordstrom.ds.autoerroretry.service;

import com.nordstrom.ds.autoerroretry.model.ConnectionSettings;

import java.util.List;

public interface Publisher {

    public void publish(final ConnectionSettings connectionSettings, final List<String> messages);

}
