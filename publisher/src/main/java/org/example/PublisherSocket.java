package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class PublisherSocket {
    private Socket socket;
    private PrintWriter out;
    private static final ObjectMapper mapper = new ObjectMapper();

    public PublisherSocket(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        System.out.println("Publisher connected to broker at " + ip + ":" + port);
    }

    public void send(Payload payload) throws IOException {
        String json = mapper.writeValueAsString(payload);
        out.println(json); // trimite JSON + newline
        System.out.println("Publisher sent: " + json);
    }

    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("Publisher disconnected.");
        }
    }
}
