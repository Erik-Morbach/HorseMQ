package network.operations;

import network.InputUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class ConsumerRegistration extends Operation {
    public static final byte CONNECT_BYTE = 0b010000;
    public ConsumerRegistration(InputStream is, OutputStream os) {
        super(is, os);
    }

    @Override
    public void handle(byte headerByte) throws IOException {
        if ((headerByte & CONNECT_BYTE) == CONNECT_BYTE) subscribeConsumer();
        else unsubscribeConsumer();
    }
    public void subscribeConsumer() throws IOException {
        String consumerId = InputUtils.readString(is).toString();
        String queueId = InputUtils.readString(is).toString();

        System.out.println("Trying to connect consumer " + consumerId + " to queue " + queueId);
        Random r = new Random();
        if(r.nextBoolean()){
            System.out.println("Connected successfully");
            os.write(0b1);
        }
        else {
            System.out.println("Not possible to connect");
            os.write(0b0);
        }
    }
    public void unsubscribeConsumer() throws IOException {
        String consumerId = InputUtils.readString(is).toString();
        String queueId = InputUtils.readString(is).toString();
        System.out.println("Unsubscribing consumer " + consumerId + " to queue " + queueId);
        os.write(0b1);
    }
}
