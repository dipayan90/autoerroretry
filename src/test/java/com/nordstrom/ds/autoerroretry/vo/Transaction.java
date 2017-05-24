package com.nordstrom.ds.autoerroretry.vo;

import java.io.Serializable;


public class Transaction implements Serializable {

    private int transactionId;
    private String date;
    private int value;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", date='" + date + '\'' +
                ", value=" + value +
                '}';
    }
}
