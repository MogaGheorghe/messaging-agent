package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BrokerSocket {
    private final ServerSocket _socket;
    private final List<ConnectionInfo> connections = new ArrayList<>();

    public BrokerSocket(String ip, int port, int maxConnections) throws IOException {
        _socket = new ServerSocket(port, maxConnections, InetAddress.getByName(ip));
        System.out.println("Broker listening on port " + port);
    }

    public void Accept() {
        new Thread(this::AcceptLoop).start();
    }

    private void AcceptLoop() {
        while (true) {
            try {
                Socket clientSocket = _socket.accept();

                ConnectionInfo connection = new ConnectionInfo();
                connection.setSocket(clientSocket);
                connection.setAddress(clientSocket.getInetAddress().toString());

                synchronized (connections) {
                    connections.add(connection);
                }

                System.out.println("Client connected: " + connection.getAddress());

                new Thread(() -> HandleClient(connection)).start();

            } catch (IOException e) {
                System.out.println("Can't accept: " + e.getMessage());
                break;
            }
        }
    }

    private void HandleClient(ConnectionInfo connection) {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getSocket().getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            ObjectMapper mapper = new ObjectMapper();

            while ((line = in.readLine()) != null) {
                // SUBSCRIBE
                if (line.startsWith("subscribe#")) {
                    String topic = line.split("#")[1];
                    connection.setTopic(topic);
                    System.out.println("[" + connection.getAddress() + "] subscribed to topic: " + topic);
                }
                // PAYLOAD de la Publisher
                else if (line.startsWith("{")) {
                    Payload payload = mapper.readValue(line, Payload.class);
                    System.out.println("[" + connection.getAddress() + "] published: " + payload);
                    sendToSubscribers(payload);
                }
            }

            System.out.println("Client disconnected: " + connection.getAddress());
        } catch (IOException e) {
            System.out.println("Error in HandleClient: " + e.getMessage());
        }
    }

    private void sendToSubscribers(Payload payload) {
        synchronized (connections) {
            for (ConnectionInfo conn : connections) {
                if (conn.getTopic() != null && conn.getTopic().equalsIgnoreCase(payload.getSender())) {
                    try {
                        PrintWriter out = new PrintWriter(
                                new OutputStreamWriter(conn.getSocket().getOutputStream(), StandardCharsets.UTF_8),
                                true
                        );
                        out.println(new ObjectMapper().writeValueAsString(payload));
                        System.out.println("Sent to subscriber [" + conn.getAddress() + "] " + payload);
                    } catch (IOException e) {
                        System.out.println("Failed to send to " + conn.getAddress() + ": " + e.getMessage());
                    }
                }
            }
        }
    }

    public void Close() throws IOException {
        if (_socket != null && !_socket.isClosed()) {
            _socket.close();
            System.out.println("Broker stopped.");
        }
    }
}
