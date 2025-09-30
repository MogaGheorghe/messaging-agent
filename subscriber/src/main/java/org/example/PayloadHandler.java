package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
public class PayloadHandler {
    public static void handle(byte[] payloadBytes) {
        try {
            String payloadString = new String(payloadBytes, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            Payload payload = mapper.readValue(payloadString, Payload.class);
            System.out.println(payload.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
