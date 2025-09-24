package network.operations;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConsumerReceive extends Operation {
    public ConsumerReceive(InputStream is, OutputStream os) {
        super(is, os);
    }

    @Override
    public void handle(byte headerByte) {

    }
}
