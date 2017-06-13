package com.nordstrom.ds.autoerroretry.service.tape;

import com.nordstrom.ds.autoerroretry.model.ConnectionSettings;
import com.nordstrom.ds.autoerroretry.service.Publisher;
import com.squareup.tape.QueueFile;

import java.io.IOException;
import java.util.List;

public class TapePublisher implements Publisher {

    private static TapePublisher tapePublisher;

    private TapePublisher(){}

    public static TapePublisher getTapePublisher(){
        if(tapePublisher == null){
            tapePublisher = new TapePublisher();
        }
        return tapePublisher;
    }

    @Override
    public void publish(ConnectionSettings connectionSettings, List<String> messages) {
        try {
            QueueFile qf = TapeQueueFile.TapeQueueFile().getQueueFile(connectionSettings.getProperties().getProperty("filename"));
            messages.forEach(message -> {
                try {
                    qf.add(message.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
