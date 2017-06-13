package com.nordstrom.ds.autoerroretry.service;

import com.nordstrom.ds.autoerroretry.model.ConnectionSettings;
import com.nordstrom.ds.autoerroretry.model.ReceivedMessage;

public interface Receiver {

    public ReceivedMessage receive(final ConnectionSettings connectionSettings);

}
