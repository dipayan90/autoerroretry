package com.nordstrom.ds.autoerroretry.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class ReceivedMessage {
    public ReceivedMessage(){}
    public ReceivedMessage(List<String> messageBody){
        this.messageBody = messageBody;
        this.messageWrapper = new ArrayList<>();
    }
    public ReceivedMessage(List<String> messageBody,List<Object> messageWrapper){
        this.messageBody = messageBody;
        this.messageWrapper = messageWrapper;
    }
    List<String> messageBody;
    List<Object> messageWrapper;
}
