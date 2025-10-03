package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Worker {
    private final int TIME_TO_WAIT = 500;
    public void DoSendMessageWork() throws InterruptedException {
        while(true){

            while(!PayloadStorage.IsEmpty())
            {
                Payload payload = PayloadStorage.GetNext();
                if(payload != null)
                {
                    List<ConnectionInfo> connections = ConnectionsStorage.GetConnectionsBySubject(payload.getSubject());
                    for (ConnectionInfo connection : connections) {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            String payloadString = mapper.writeValueAsString(payload);


                            byte[] data = payloadString.getBytes(StandardCharsets.UTF_8);

                            OutputStream out = connection.getSocket().getOutputStream();
                            out.write((payloadString + "\n").getBytes(StandardCharsets.UTF_8));
                            out.flush();

                        } catch (Exception e) {
                            System.out.println("Failed to send payload to " + connection.getAddress() + ": " + e.getMessage());
                        }
                    }
                }

            }

            Thread.sleep(TIME_TO_WAIT);


        }
    }
}
