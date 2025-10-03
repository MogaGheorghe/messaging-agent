package org.example;

import java.io.Serializable;

public class Payload implements Serializable {
    private String subject;
    private String message;
    private final long timestamp;

    public Payload() {
        this.timestamp = System.currentTimeMillis();
    }

    public Payload(String subject, String message) {
        this.subject = subject;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String sender) {
        this.subject = sender;
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
        return "[" + subject + " @ " + timestamp + "] " + message;
    }
}
