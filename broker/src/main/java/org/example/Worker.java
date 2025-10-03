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
                            // Serialize payload to JSON
                            ObjectMapper mapper = new ObjectMapper();
                            String payloadString = mapper.writeValueAsString(payload);

                            // Convert to bytes (UTF-8 encoding)
                            byte[] data = payloadString.getBytes(StandardCharsets.UTF_8);

                            // Send to client
                            OutputStream out = connection.getSocket().getOutputStream();
                            out.write(data);
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
