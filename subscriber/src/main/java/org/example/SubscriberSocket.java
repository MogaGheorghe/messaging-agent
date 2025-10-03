package org.example;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class SubscriberSocket {
    private Socket socket;
    private String topic;

    public SubscriberSocket(String topic) {
        this.topic = topic;
        try {
            this.socket = new Socket(InetAddress.getByName("localhost"), Settings.BROKER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(String ipAddress, int port) {
        new Thread(() -> {
            try {
                if (socket == null || socket.isClosed()) {
                    socket.connect(new InetSocketAddress(ipAddress, port));
                }
                connectedCallback();
            } catch (IOException e) {
                System.out.println("Error: Subscriber connection failed.");
                e.printStackTrace();
            }
        }).start();

        System.out.println("Waiting for a connection");
    }

    private void connectedCallback() {
        if (socket.isConnected()) {
            System.out.println("Subscriber connected to broker.");
            Subscribe();
            StartReceive();
        } else {
            System.out.println("Error: Subscriber not connected.");
        }
    }

    private void Subscribe() {
        try {
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
                    true
            );
            out.println("subscribe#" + topic);
            System.out.println("Sent subscribe request for topic: " + topic);
        } catch (IOException e) {
            System.out.println("Could not send subscribe request: " + e.getMessage());
        }
    }

    private void StartReceive() {
        ConnectionInfo connection = new ConnectionInfo();
        connection.Socket = socket;
        new Thread(() -> receiveCallback(connection)).start();
    }

    private void receiveCallback(ConnectionInfo connection) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getSocket().getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                Payload payload = new com.fasterxml.jackson.databind.ObjectMapper().readValue(line, Payload.class);
                System.out.println("Received message: " + payload);
            }
        } catch (Exception e) {
            System.out.println("Can't receive data from broker. " + e.getMessage());
            try {
                connection.getSocket().close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
