package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class BrokerSocket {
    private final ServerSocket  _socket;

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
                Payload payload = mapper.readValue(line, Payload.class);
                System.out.println("[" + connection.getAddress() + "] " + payload);
            }
            System.out.println("Client disconnected: " + connection.getAddress());
        } catch (IOException e) {
            System.out.println("Error in HandleClient: " + e.getMessage());
        }
    }
//    private void HandleClient(ConnectionInfo connection) {
//        try (ObjectInputStream in = new ObjectInputStream(connection.getSocket().getInputStream());
//             ObjectOutputStream out = new ObjectOutputStream(connection.getSocket().getOutputStream())) {
//
//            Object obj;
//            while ((obj = in.readObject()) != null) {
//                if (obj instanceof Payload payload) {
//                    System.out.println("[" + connection.getAddress() + "] " + payload);
//
//                    // trimitem payload către grupul multicast
//                    // sendToSubscribers(payload);
//                }
//            }
//
//            System.out.println("Client disconnected: " + connection.getAddress());
//
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    public void Close() throws IOException {
        if (_socket != null && !_socket.isClosed()) {
            _socket.close();
            System.out.println("Broker stopped.");
        }
    }

//    private void sendToSubscribers(Payload payload) {
//        try {
//            InetAddress group = InetAddress.getByName("224.0.0.224");
//            int port = Settings.BROKER_PORT;
//
//            byte[] data = payload.toString().getBytes("UTF-16");
//
//            DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
//
//            try (DatagramSocket datagramSocket = new DatagramSocket()) {
//                datagramSocket.send(packet);
//            }
//
//            System.out.println("Payload multicast to subscribers: " + payload);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
