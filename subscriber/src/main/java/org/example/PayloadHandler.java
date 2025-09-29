package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
public class PayloadHandler {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void Handle(byte[] payloadBytes) {
        try {
            String payloadString = new String(payloadBytes, StandardCharsets.UTF_8);
            Payload payload = mapper.readValue(payloadString, Payload.class);
            System.out.println(payload.getMessage());
        } catch (IOException e) {
            System.out.println("Error deserializing payload: " + e.getMessage());
        }
    }
}
