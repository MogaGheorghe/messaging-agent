package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SubscriberSocket {
    private Socket socket;
    private String topic;

    public SubscriberSocket(String topic) {
        this.topic = topic;
        this.socket = new Socket();
    }

    public void connect(String ipAddress, int port) {
        new Thread(() -> {
            try {
                socket.connect(new InetSocketAddress(ipAddress, port));
                System.out.println("Waiting for a connection...");
                connectedCallback();
            } catch (IOException e) {
                System.out.println("Error: Subscriber could not connect to broker. " + e.getMessage());
            }
        }).start();
    }

    private void connectedCallback() {
        if (socket.isConnected()) {
            System.out.println("Subscriber connected to broker.");
            subscribe();
            startReceive();
        } else {
            System.out.println("Error: Subscriber could not connect to broker.");
        }
    }

    private void subscribe() {
        String msg = "subscribe#" + topic;
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        send(data);
    }

    private void send(byte[] data) {
        try {
            socket.getOutputStream().write(data);
            socket.getOutputStream().flush();
        } catch (IOException e) {
            System.out.println("Could not send data: " + e.getMessage());
        }
    }

    private void startReceive() {
        ConnectionInfo connection = new ConnectionInfo();
        connection.setSocket(socket);
        new Thread(() -> receiveLoop(connection)).start();
    }


    private void receiveLoop(ConnectionInfo connection) {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getSocket().getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = in.readLine()) != null) {
                Payload payload = new ObjectMapper().readValue(line, Payload.class);
                System.out.println("Subscriber received: " + payload.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Can't receive data from broker: " + e.getMessage());
        }
    }
//    private void receiveLoop(ConnectionInfo connection) {
//        try {
//            while (true) {
//                int read = connection.getSocket().getInputStream().read(connection.getData());
//                if (read == -1) break;
//
//                byte[] payloadBytes = new byte[read];
//                System.arraycopy(connection.getData(), 0, payloadBytes, 0, read);
//
//                PayloadHandler.Handle(payloadBytes);
//            }
//        } catch (IOException e) {
//            System.out.println("Can't receive data from broker: " + e.getMessage());
//        } finally {
//            try {
//                connection.getSocket().close();
//            } catch (IOException ignored) {}
//        }
//    }
}