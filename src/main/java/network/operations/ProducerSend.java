package network.operations;

import core.DoubleMessage;
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
    private final double messageDuration = 0.1;
    private final double sampleRate = 44100.0;
    private final int bufferSize = (int)(messageDuration * sampleRate) * 8;
    private ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
    private long lastSendTime = System.currentTimeMillis();
    private long maxDiff = 0;

    public ProducerSend(InputStream is, OutputStream os, InputExchanger inputExchanger, OutputExchanger outputExchanger) {
        super(is, os, inputExchanger, outputExchanger);
    }

    @Override
    public void handle(byte headerByte) throws IOException {
        maxDiff = 0;
        String producerId = InputUtils.readString(is).toString();
        String queueId = InputUtils.readString(is).toString();

        System.out.println("Producer ID: " + producerId);
        System.out.println("Queue ID: " + queueId);
        System.out.println("Connection successful");
        os.write(0b1);
        os.flush();

        buffer.order(ByteOrder.BIG_ENDIAN);
        int blockSize = 0;

        int bufferIndex = 0;
        while((blockSize = InputUtils.readInt(is)) > 0) {
            updateLatency();
            for(int i=0;i<blockSize;i++) {
                buffer.putDouble(bufferIndex*8, InputUtils.readDouble(is));
                bufferIndex++;
                if(bufferIndex*8 >= bufferSize) {
                    inputExchanger.receiveMessage(queueId, producerId, DoubleMessage.from(buffer));
                    bufferIndex = 0;
                }
            }
        }
        System.out.println("Ending connection");
    }

    private void updateLatency(){
        long currentTime = System.currentTimeMillis();
        maxDiff = Math.max(currentTime - lastSendTime, maxDiff);
        System.out.printf("diff %d       \r",maxDiff);
        lastSendTime = currentTime;
    }
}
