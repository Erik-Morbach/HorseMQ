package network.operations;

import network.InputUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

public class Create extends Operation {
    public static final byte TYPE_BYTE = 0b0010000;
    public static final byte USER_BYTE = 0b0001000;
    public Create(InputStream is, OutputStream os) {
        super(is, os);
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
        Random rand = new Random();
        StringBuilder producerId = new StringBuilder();
        for(int i=0;i<4;i++) producerId.append((char) rand.nextInt('a', 'z'));
        os.write(producerId.toString().getBytes());
        System.out.println("Creating producer: " + producerName + " with id: " +producerId);
    }

    public void createConsumer() throws IOException {
        String consumerName = InputUtils.readString(is).toString();
        Random rand = new Random();
        StringBuilder consumerId = new StringBuilder();
        for(int i=0;i<4;i++) consumerId.append((char) rand.nextInt('a', 'z'));
        os.write(consumerId.toString().getBytes());
        System.out.println("Created consumer: " + consumerName + " with id: " + consumerId);
    }
    public void createQueue() throws IOException {
        List<String> allowedConsumers = List.of(InputUtils.readString(this.is).toString().split(";"));
        int c;
        System.out.println("Creating queue for consumers: " + allowedConsumers.stream().reduce("", (a, b) -> a +","+ b));
        String queueName = "TesQ";
        os.write(queueName.getBytes());
    }

}
