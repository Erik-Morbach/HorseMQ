package network.operations;

import network.InputUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

public class Create extends Operation {
    public static final byte TYPE_BYTE = 0b00100000;
    public Create(InputStream is, OutputStream os) {
        super(is, os);
    }

    @Override
    public void handle(byte headerByte) throws IOException {
        if((headerByte & TYPE_BYTE) == TYPE_BYTE) createQueue(headerByte);
        else createUser(headerByte);
    }

    public void createUser(byte headerByte){
    }

    public void createQueue(byte headerByte) throws IOException {
        List<String> allowedConsumers = List.of(InputUtils.readString(this.is).toString().split(";"));
        int c;
        System.out.println("Creating queue for consumers: " + allowedConsumers.stream().reduce("", (a, b) -> a +","+ b));
        String queueName = "TesQ";
        os.write(queueName.getBytes());
        os.write('\0');
    }

}
