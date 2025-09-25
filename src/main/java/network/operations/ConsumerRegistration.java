package network.operations;

import core.InputExchanger;
import core.OutputExchanger;
import network.InputUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class ConsumerRegistration extends Operation {
    public static final byte CONNECT_BYTE = 0b010000;

    public ConsumerRegistration(InputStream is, OutputStream os, InputExchanger inputExchanger, OutputExchanger outputExchanger) {
        super(is, os, inputExchanger, outputExchanger);
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
        boolean value = outputExchanger.firstQueueConnection(consumerId, queueId);
        os.write(value? 1: 0);
        System.out.println(value?"Connected successfully":"Not able to connect");
    }
    public void unsubscribeConsumer() throws IOException {
        String consumerId = InputUtils.readString(is).toString();
        String queueId = InputUtils.readString(is).toString();
        System.out.println("Unsubscribing consumer " + consumerId + " to queue " + queueId);
        os.write(outputExchanger.disconnectFromQueue(consumerId, queueId)?1:0);
    }
}
