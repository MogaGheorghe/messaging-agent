package org.example;

import java.io.Serializable;

public class Payload implements Serializable {
    private String sender;
    private String message;
    private final long timestamp;

    public Payload() {
        this.timestamp = System.currentTimeMillis();
    }

    public Payload(String sender, String message) {
        this.sender = sender;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[" + sender + " @ " + timestamp + "] " + message;
    }
}
