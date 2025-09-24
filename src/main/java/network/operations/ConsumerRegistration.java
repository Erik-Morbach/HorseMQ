package network.operations;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConsumerRegistration extends Operation {
    public ConsumerRegistration(InputStream is, OutputStream os) {
        super(is, os);
    }

    @Override
    public void handle(byte headerByte) {

    }
}
