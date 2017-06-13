package com.nordstrom.ds.autoerroretry.service.tape;

import com.amazonaws.services.sqs.model.Message;
import com.nordstrom.ds.autoerroretry.model.ConnectionSettings;
import com.nordstrom.ds.autoerroretry.model.ReceivedMessage;
import com.nordstrom.ds.autoerroretry.service.Receiver;
import com.squareup.tape.QueueFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TapeReceiver implements Receiver {

    private static TapeReceiver tapeReceiver;

    private TapeReceiver(){}

    public static TapeReceiver getTapeReceiver(){
        if(tapeReceiver == null){
            tapeReceiver = new TapeReceiver();
        }
        return tapeReceiver;
    }

    @Override
    public ReceivedMessage receive(ConnectionSettings connectionSettings) {
        List<String> messages = new ArrayList<>();
        try {
            QueueFile qf = TapeQueueFile.TapeQueueFile().getQueueFile(connectionSettings.getProperties().getProperty("filename"));
            while (!qf.isEmpty()){
                messages.add(new String(qf.peek()));
                qf.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ReceivedMessage(messages);
    }

}
