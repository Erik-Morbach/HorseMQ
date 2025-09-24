package network.operations;

import network.InputUtils;

import javax.xml.transform.Source;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ProducerSend extends Operation {
    private double[] buffer = new double[1024];
    public ProducerSend(InputStream is, OutputStream os) {
        super(is, os);
    }

    @Override
    public void handle(byte headerByte) throws IOException {
        String producerId = InputUtils.readString(is).toString();
        String queueId = InputUtils.readString(is).toString();

        System.out.println("Producer ID: " + producerId);
        System.out.println("Queue ID: " + queueId);
        System.out.println("Connection successful");
        os.write(0b1);
        os.flush();

        int blockSize = 0;
        while((blockSize = InputUtils.readInt(is)) > 0) {
            for(int i=0;i<blockSize;i++) {
                buffer[i] = InputUtils.readDouble(is);
            }
            System.out.println("Read block with "+blockSize +" values");
        }
        System.out.println("Ending connection");

    }
}
