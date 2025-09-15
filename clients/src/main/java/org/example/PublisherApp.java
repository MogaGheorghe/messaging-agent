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
            System.out.print("Enter your name: ");
            String sender = scanner.nextLine();

            while (true) {
                System.out.print("Message (or 'exit' to quit): ");
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("exit")) break;

                Payload payload = new Payload(sender, message);
                publisher.send(payload);

                // Receive broker's reply (for now, just echo)
                Object reply = publisher.receive();
                System.out.println("Broker reply: " + reply);
            }

            publisher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}