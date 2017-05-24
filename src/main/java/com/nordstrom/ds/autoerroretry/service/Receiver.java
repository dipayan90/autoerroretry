package com.nordstrom.ds.autoerroretry.service;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

import java.util.List;

public interface Receiver {

    public List<Message> receive(final String endpoint);

    public void deleteMessagesFromQueue(List<Message> messages, final String sqsUrl);
}
