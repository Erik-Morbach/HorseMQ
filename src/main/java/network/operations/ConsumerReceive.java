package network.operations;

import network.InputUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Random;

public class ConsumerReceive extends Operation {
    private ByteBuffer buffer = ByteBuffer.allocate(1024*8);
    private int bufferSeparation = 1024*8 - 100;
    public ConsumerReceive(InputStream is, OutputStream os) {
        super(is, os);
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

        int currentSize = rand.nextInt(10,100);
        for(int i=0;i<currentSize;i++){
            buffer.putDouble(i, rand.nextDouble());
        }

        int blockSize = 0;
        int beginIndex = 0;
        ByteBuffer intBuffer = ByteBuffer.allocate(4);
        while((blockSize = InputUtils.readInt(is)) > 0) {
            System.out.println("Desired "+ blockSize);
            int size = Math.max(0, Math.min(currentSize - beginIndex, blockSize));
            intBuffer.putInt(0, size);
            System.out.println("Sending " + size + " bytes");
            os.write(intBuffer.array());
            os.flush();

            if(size == 0) break;

            os.write(buffer.array(), beginIndex, size*8);
            os.flush();
            beginIndex += size;
            System.out.println("Read block with "+blockSize +" values");
        }
        os.write(0b11111111);
        os.flush();
        System.out.println("Ending connection");
    }
}
