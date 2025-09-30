package org.example;

import java.util.Scanner;

public class SubscriberApp {
    public static void main(String[] args) {

        System.out.println("Subscriber");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the topic: ");
        String topic = scanner.nextLine().toLowerCase();

        SubscriberSocket subscriberSocket = new SubscriberSocket(topic);
        subscriberSocket.connect(Settings.BROKER_IP, Settings.BROKER_PORT);

        System.out.println("Press ENTER to exit...");
        scanner.nextLine();

        System.out.println("Subscriber stopped.");
        System.exit(0);

    }
}
