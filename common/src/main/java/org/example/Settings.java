package org.example;


// Comanda pentru a vedea porturile folosite:
// netstat -an | find /i "listening"

public class Settings {
   public static int BROKER_PORT = 9000;
   public static int BROKER_CONNECTIONS_LIMIT = 10;
   public static String BROKER_IP = "127.0.0.1";

}