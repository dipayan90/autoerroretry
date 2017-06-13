package com.nordstrom.ds.autoerroretry.service.tape;


import com.squareup.tape.QueueFile;

import java.io.File;
import java.io.IOException;

public class TapeQueueFile {

    private QueueFile queueFile;
    private static TapeQueueFile tapeQueueFile;

    private TapeQueueFile(){}

    public static TapeQueueFile TapeQueueFile(){
        if(tapeQueueFile == null){
            tapeQueueFile = new TapeQueueFile();
        }
        return tapeQueueFile;
    }

    public QueueFile getQueueFile(String fileName) throws IOException {
        if(queueFile == null){
            File file = new File(fileName);
            queueFile =  new QueueFile(file);
        }
        return queueFile;
    }
}
