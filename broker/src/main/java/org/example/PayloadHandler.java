package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PayloadHandler {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void Handle(byte[] payloadBytes, ConnectionInfo connection) {
        String payloadString = new String(payloadBytes, StandardCharsets.UTF_8).trim();

        try {
            if (payloadString.startsWith("subscribe#")) {
                String topic = payloadString.split("#", 2)[1].trim();
                connection.setTopic(topic);

                ConnectionsStorage.Add(connection);
                System.out.printf("[%s] Subscribed to topic: %s%n", connection.getAddress(), topic);
                return;
            }
            Payload payload = mapper.readValue(payloadString, Payload.class);
            System.out.printf("[%s] Published payload: %s%n", connection.getAddress(), payload);

            PayloadStorage.Add(payload);

        } catch (IOException e) {
            System.out.printf("[%s] Invalid message format: %s%n", connection.getAddress(), payloadString);
        }
    }
}