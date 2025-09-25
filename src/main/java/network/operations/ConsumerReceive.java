package network.operations;

import core.DoubleMessage;
import core.InputExchanger;
import core.OutputExchanger;
import network.InputUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Random;

public class ConsumerReceive extends Operation {
    private ByteBuffer buffer = ByteBuffer.allocate(1024*8);
    private final double messageDuration = 0.1;
    private final double sampleRate = 44100.0;
    private final int bufferSize = (int)(messageDuration * sampleRate * 8);

    public ConsumerReceive(InputStream is, OutputStream os, InputExchanger inputExchanger, OutputExchanger outputExchanger) {
        super(is, os, inputExchanger, outputExchanger);
    }

    @Override
    public void handle(byte headerByte) throws IOException {
        String consumerId = InputUtils.readString(is).toString();
        String queueId = InputUtils.readString(is).toString();

        System.out.println("Consumer ID: " + consumerId);
        System.out.println("Queue ID: " + queueId);
        System.out.println("Connection successful");
        os.write(0b1);
        os.flush();

        Random rand = new Random();

        int blockSize = 0;
        ByteBuffer intBuffer = ByteBuffer.allocate(4);
        intBuffer.order(ByteOrder.BIG_ENDIAN);
        buffer.order(ByteOrder.BIG_ENDIAN);
        int index = 0;
        while((blockSize = InputUtils.readInt(is)) > 0) {
            int size = blockSize;
            intBuffer.putInt(0, size);
            os.write(intBuffer.array(),0, 4);
            os.flush();

            if(size == 0) continue;

            for(int i=0;i<size;i++){
                DoubleMessage message = new DoubleMessage();
                buffer.position(0);
                for(int j=0;j<bufferSize;j++) {
                    buffer.putDouble(j * 8, message.values.get(j));
                }
                os.write(buffer.array(), 0, size*8);
                os.flush();
            }
        }
        while (true) {
            int maxMessages = InputUtils.readInt(is);
            List<DoubleMessage> batch = outputExchanger.sendMessage(maxMessages, consumerId, queueId);
            int size = batch.stream().mapToInt(m -> m.values.size()).sum();

            intBuffer.putInt(0, size);
            os.write(intBuffer.array(), 0, 4);
            os.flush();

            if (size == 0) continue;

            int offset = 0;
            for (DoubleMessage msg : batch) {
                for (double d : msg.values) {
                    buffer.putDouble(offset, d);
                    offset += 8;
                }
            }

            os.write(buffer.array(), 0, size*8);
            os.flush();
        }
    }
}
