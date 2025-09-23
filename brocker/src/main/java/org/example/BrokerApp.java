package org.example;



public class BrokerApp {
    public static void main(String[] args) {
        System.out.println("BrokerApp started");
        try {
            BrokerSocket broker = new BrokerSocket();
            broker.Start
            (Settings.BROKER_IP, Settings.BROKER_PORT, Settings.BROKER_CONNECTIONS_LIMIT);

            Worker worker = new Worker();

            Thread workerThread = new Thread(() -> {
                try {
                    worker.DoSendMessageWork();
                } catch (InterruptedException e) {
                    System.out.println("Worker interrupted: " + e.getMessage());
                    Thread.currentThread().interrupt(); // restore interrupt flag
                }
            });

            workerThread.setDaemon(true); // optional, so JVM can exit when main ends
            workerThread.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

