package org.example;

import java.net.Socket;

public class ConnectionInfo {
    public final int BUFFER_SIZE = 1024;
    public byte[] Data;
    public Socket Socket;
    public String Address;
    public String Topic;

    public ConnectionInfo() {
        Data = new byte[BUFFER_SIZE];
    }

    public Socket getSocket() {
        return Socket;
    }
    public void setSocket(Socket socket) {
        Socket = socket;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public byte[] getData() {
        return Data;
    }

    public void setData(byte[] data) {
        Data = data;
    }


}
