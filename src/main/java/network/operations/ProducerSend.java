package network.operations;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ProducerSend extends Operation {
    public ProducerSend(InputStream is, OutputStream os) {
        super(is, os);
    }

    @Override
    public void handle(byte headerByte) {

    }
}
