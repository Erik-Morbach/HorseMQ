package network.operations;

import core.InputExchanger;
import core.OutputExchanger;
import network.InputUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

public class Create extends Operation {
    public static final byte TYPE_BYTE = 0b0010000;
    public static final byte USER_BYTE = 0b0001000;

    public Create(InputStream is, OutputStream os, InputExchanger inputExchanger, OutputExchanger outputExchanger) {
        super(is, os, inputExchanger, outputExchanger);
    }

    @Override
    public void handle(byte headerByte) throws IOException {
        if((headerByte & TYPE_BYTE) == TYPE_BYTE) createQueue();
        else createUser(headerByte);
    }

    public void createUser(byte headerByte) throws IOException {
        if((headerByte & USER_BYTE) == USER_BYTE) createProducer();
        else createConsumer();
    }

    public void createProducer() throws IOException {
        String producerName = InputUtils.readString(is).toString();
        System.out.println("Creating producer: " + producerName);
        os.write(inputExchanger.registerProducer(producerName)?1:0);
        os.flush();
    }

    public void createConsumer() throws IOException {
        String consumerName = InputUtils.readString(is).toString();
        System.out.println("Created consumer: " + consumerName);
        os.write(inputExchanger.registerConsumer(consumerName)?1:0);
        os.flush();
    }
    public void createQueue() throws IOException {
        String producerId = InputUtils.readString(is).toString();
        String queueId = InputUtils.readString(is).toString();
        List<String> allowedConsumers = List.of(InputUtils.readString(this.is).toString().split(";"));
        System.out.println("Creating queue " + queueId + " for consumers: " + allowedConsumers.stream().reduce("", (a, b) -> a +","+ b));
        os.write(inputExchanger.registerQueue(queueId, producerId, allowedConsumers)?1:0);
        os.flush();
    }

}
