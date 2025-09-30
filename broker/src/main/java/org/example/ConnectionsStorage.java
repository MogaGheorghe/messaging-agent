package org.example;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ConnectionsStorage {

    private static final List<ConnectionInfo> _connections = new CopyOnWriteArrayList<>();

    public static void Add(ConnectionInfo connection) {
        _connections.add(connection);
        System.out.println("Connection added: " + connection.getAddress());
    }

    public static void Remove(String address) {
        _connections.removeIf(c -> c.getAddress().equals(address));
        System.out.println("Connection removed: " + address);
    }

    public static List<ConnectionInfo> GetAll() {
        return _connections;
    }

    public static int Count() {
        return _connections.size();
    }

    public static List<ConnectionInfo> GetConnectionsBySubject(String topic) {
        return _connections.stream()
                .filter(c -> topic.equals(c.getTopic()))
                .collect(Collectors.toList());
    }
}
