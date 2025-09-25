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
    private final double messageDuration = 0.1;
    private final double sampleRate = 44100.0;
    private final int bufferSize = (int)(messageDuration * sampleRate) * 8;
    private ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

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
        while (true) {
            int maxMessages = InputUtils.readInt(is);
            List<Double> batch = outputExchanger.sendMessage(maxMessages, consumerId, queueId);
            int size = batch.size();

            intBuffer.putInt(0, size);
            os.write(intBuffer.array(), 0, 4);
            os.flush();

            if (size == 0) continue;

            int offset = 0;
            for (Double msg : batch) {
                buffer.putDouble(offset, msg);
                offset += 8;
            }

            os.write(buffer.array(), 0, size*8);
            os.flush();
        }
    }
}
