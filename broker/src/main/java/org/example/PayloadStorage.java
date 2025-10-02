package org.example;

import java.util.concurrent.ConcurrentLinkedQueue;

public class PayloadStorage {

    private static final ConcurrentLinkedQueue<Payload> payloadQueue;

    static {
        payloadQueue = new ConcurrentLinkedQueue<>();
    }

    public static void Add(Payload payload) {
        payloadQueue.add(payload);
    }

    public static Payload GetNext() {
        return payloadQueue.poll();
    }

    public static Payload Peek() {
        return payloadQueue.peek();
    }


    public static boolean IsEmpty() {
        return payloadQueue.isEmpty();
    }
}
