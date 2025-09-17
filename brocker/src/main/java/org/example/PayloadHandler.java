package org.example;

public class PayloadHandler {
    public static void Handle(byte[] payload, ConnectionInfo connection) {
        System.out.println("Payload: " + new String(payload));
    }
}
