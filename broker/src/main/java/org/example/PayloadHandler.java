package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class PayloadHandler {
    public static void Handle(byte[] payload, ConnectionInfo connection) {

        String payloadString = new String(payload);

        if (payloadString.startsWith("#subscribe")) {
            connection.setTopic(payloadString.split("#subscribe")[1]);
            //adaugam conexiunea in storage
            ConnectionsStorage.Add(connection);
            System.out.println("Subscribed to topic");
        }
        else
        {
            System.out.printf("[%s] Payload: %s%n", connection.getAddress(), payloadString);
            ObjectMapper mapper = new ObjectMapper();
            try {
                Payload p = mapper.readValue(payloadString, Payload.class);
                System.out.println("Parsed subject: " + p.getSubject());
                PayloadStorage.Add(p);
            } catch (IOException e) {
                System.out.printf("[%s] Invalid JSON: %s%n", connection.getAddress(), e.getMessage());
            }



        }


    }
}
