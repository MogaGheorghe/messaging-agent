package org.example;

import java.net.Socket;

import static org.example.Settings.BUFFER_SIZE;

public class ConnectionInfo {
    public byte[] Data;
    public Socket Socket;
    public String Address;
    public String Subject;

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
        return Subject;
    }

    public void setTopic(String topic) {
        Subject = topic;
    }

    public byte[] getData() {
        return Data;
    }

    public void setData(byte[] data) {
        Data = data;
    }


}
