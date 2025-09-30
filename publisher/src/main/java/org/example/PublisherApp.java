package org.example;

import java.util.Scanner;

public class PublisherApp {

    public static void main(String[] args) {
        System.out.println("PublisherApp started");

        try {
            PublisherSocket publisher = new PublisherSocket(
                    Settings.BROKER_IP,
                    Settings.BROKER_PORT
            );

            Scanner scanner = new Scanner(System.in);

            while (true) {
                // Aleg topicul
                System.out.print("Enter the topic (or 'exit' to quit): ");
                String topic = scanner.nextLine();
                if (topic.equalsIgnoreCase("exit")) break;

                // Scriu mesajul
                System.out.print("Message (or 'exit' to quit): ");
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("exit")) break;

                // Construiesc payload
                Payload payload = new Payload(topic, message);
                publisher.send(payload);

                // Primim răspunsul broker-ului
//                Payload reply = publisher.receive();
//                System.out.println("Broker reply: " + reply);
//
//                // Serializare în JSON
//                byte[] data = PublisherSocket.serializeToJson(payload);
//                publisher.sendBytes(data);
            }

            publisher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}