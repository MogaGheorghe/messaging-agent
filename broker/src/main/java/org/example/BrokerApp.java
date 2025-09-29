package org.example;

public class BrokerApp {
    public static void main(String[] args) {
        System.out.println("BrokerApp started");

        try {
            BrokerSocket broker = new BrokerSocket(
                    Settings.BROKER_IP,
                    Settings.BROKER_PORT,
                    Settings.BROKER_CONNECTIONS_LIMIT
            );

            broker.Accept(); // start accepting clients

            // keep main thread alive so broker runs
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

