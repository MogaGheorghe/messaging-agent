package org.example;

import java.io.*;
import java.net.Socket;

public class PublisherSocket {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public PublisherSocket(String ip, int port) throws IOException {
        socket = new Socket(ip, port);

        // Object streams for sending/receiving Payload
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        System.out.println("Publisher connected to broker at " + ip + ":" + port);
    }

    public void send(Payload payload) throws IOException {
        out.writeObject(payload);
        out.flush();
        System.out.println("Publisher sent: " + payload);
    }

    public Object receive() throws IOException, ClassNotFoundException {
        Object response = in.readObject();
        System.out.println("Publisher received: " + response);
        return response;
    }

    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("Publisher disconnected.");
        }
    }
}
