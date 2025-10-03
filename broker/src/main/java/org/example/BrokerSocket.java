package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import static org.example.Settings.BUFFER_SIZE;

public class BrokerSocket {
    private final ServerSocket  _socket;
    private volatile boolean running = false;

    public BrokerSocket() throws IOException {
        _socket = new ServerSocket();
    }

    public ServerSocket getSocket() {
        return _socket;
    }

    public void Start(String ip, int port, int maxConnections) throws IOException {
        _socket.bind(new InetSocketAddress(ip, port), maxConnections);
        running = true;
        System.out.println("Broker listening on port " + port + " \nMax connections " + maxConnections + "\nIP Address " + ip);
        Accept();
    }


    private void Accept() {
        new Thread(this::AcceptLoop).start();
    }

    private void AcceptLoop() {
        while (running && !_socket.isClosed()) {
            try {
                Socket clientSocket = _socket.accept();

                ConnectionInfo connection = new ConnectionInfo();
                connection.setSocket(clientSocket);
                connection.setAddress(clientSocket.getInetAddress().toString());

                System.out.println("Client connected: " + connection.getAddress());
                new Thread(() -> HandleClient(connection)).start();

            } catch (IOException e) {
                if (running) {
                    System.out.println("Can't accept: " + e.getMessage());
                }
            }
        }
        System.out.println("Stopped accepting clients.");
    }


    private void onDisconnect(String address) {
        System.out.println("Client disconnected: " + address);
        ConnectionsStorage.Remove(address);

    }

    private void HandleClient(ConnectionInfo connection) {
        try {
            Socket socket = connection.getSocket();
            InputStream input = socket.getInputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                // Copy only the valid bytes
                byte[] payload = Arrays.copyOf(buffer, bytesRead);

                // Delegate to payload handler
                PayloadHandler.Handle(payload, connection);
            }


        } catch (IOException e) {
            System.out.println("Can't receive data: " + e.getMessage());
        } finally {
            try {
                String address = connection.getAddress();
                onDisconnect(address);
                connection.getSocket().close();
            } catch (IOException ignore) {}
        }
    }

    public void Close() throws IOException {
        running = false; // signal stop
        if (_socket.isBound() && !_socket.isClosed()) {
            _socket.close();
        }
        System.out.println("Broker stopped.");
    }

}