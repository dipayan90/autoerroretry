package com.nordstrom.ds.autoerroretry.service;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.nordstrom.ds.autoerroretry.model.ConnectionSettings;
import com.nordstrom.ds.autoerroretry.model.ReceivedMessage;

import java.util.List;

public interface Receiver {

    public ReceivedMessage receive(final ConnectionSettings connectionSettings);

    public void deleteMessagesFromQueue(List<Message> messages, final ConnectionSettings connectionSettings);
}
