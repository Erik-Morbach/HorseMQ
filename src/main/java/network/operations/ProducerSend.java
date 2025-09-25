package network.operations;

import core.InputExchanger;
import core.OutputExchanger;
import network.InputUtils;

import javax.xml.transform.Source;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ProducerSend extends Operation {
    private ByteBuffer buffer = ByteBuffer.allocate(1024*8);
    private long lastSendTime = System.currentTimeMillis();

    public ProducerSend(InputStream is, OutputStream os, InputExchanger inputExchanger, OutputExchanger outputExchanger) {
        super(is, os, inputExchanger, outputExchanger);
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

        buffer.order(ByteOrder.BIG_ENDIAN);
        int blockSize = 0;
        long maxDiff = 0;
        while((blockSize = InputUtils.readInt(is)) > 0) {
            long currentTime = System.currentTimeMillis();
            maxDiff = Math.max(currentTime - lastSendTime, maxDiff);
            System.out.printf("diff %d       \r",maxDiff);
            lastSendTime = currentTime;
            for(int i=0;i<blockSize;i++) {
                buffer.putDouble(i*8, InputUtils.readDouble(is));
            }
        }
        System.out.println("Ending connection");
    }
}
