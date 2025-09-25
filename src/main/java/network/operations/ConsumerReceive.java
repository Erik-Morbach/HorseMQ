package network.operations;

import network.InputUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
                buffer.putDouble(i*8, Math.sin(2*Math.PI*2*(index++)/44100));
            }
            os.write(buffer.array(), 0, size*8);
            os.flush();
        }
        os.write(0b11111111);
        os.flush();
        System.out.println("Ending connection");
    }
}
