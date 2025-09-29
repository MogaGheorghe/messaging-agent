package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class PublisherSocket {
    private Socket socket;
    //private ObjectOutputStream out;
    private ObjectInputStream in;
    private PrintWriter out;
    private static final ObjectMapper mapper = new ObjectMapper();
    public PublisherSocket(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        System.out.println("Publisher connected to broker at " + ip + ":" + port);
    }
//    public PublisherSocket(String ip, int port) throws IOException {
//        socket = new Socket(ip, port);
//
//        // Object streams for sending/receiving Payload
//        out = new ObjectOutputStream(socket.getOutputStream());
//        in = new ObjectInputStream(socket.getInputStream());
//
//        System.out.println("Publisher connected to broker at " + ip + ":" + port);
//    }

    // trimitem Payload ca JSON
    public void send(Payload payload) throws IOException {
        String json = mapper.writeValueAsString(payload);
        out.println(json);
        System.out.println("Publisher sent: " + json);
    }
//    public void send(Payload payload) throws IOException {
//        out.writeObject(payload);
//        out.flush();
//        System.out.println("Publisher sent: " + payload);
//    }

    public Object receive() throws IOException, ClassNotFoundException {
        Object response = in.readObject();
        System.out.println("Publisher received: " + response);
        return response;
    }
    public void close() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
                System.out.println("Publisher disconnected.");
            } catch (IOException e) {
                System.out.println("Error closing the socket: " + e.getMessage());
            }
        }
    }
    // Trimite un array de bytes (JSON, text, orice)
    public void sendBytes(byte[] data) throws IOException {
        // Încapsulăm dimensiunea datelor pentru a fi citite corect pe server
        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(data.length);   // trimite întâi lungimea
        dos.write(data);             // apoi datele
        dos.flush();
        System.out.println("Publisher sent (bytes): " + new String(data, StandardCharsets.UTF_8));
    }
    public static byte[] serializeToJson(Payload payload) throws IOException {
        String json = mapper.writeValueAsString(payload);
        return json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }
}
