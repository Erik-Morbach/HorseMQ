package network.operations;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class Operation {
    protected InputStream is;
    protected OutputStream os;
    public Operation(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }
    public abstract void handle(byte headerByte) throws IOException;

}
