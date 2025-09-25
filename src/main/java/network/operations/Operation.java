package network.operations;

import core.InputExchanger;
import core.OutputExchanger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class Operation {
    protected InputStream is;
    protected OutputStream os;
    protected InputExchanger inputExchanger;
    protected OutputExchanger outputExchanger;
    public Operation(InputStream is, OutputStream os, InputExchanger inputExchanger, OutputExchanger outputExchanger) {
        this.is = is;
        this.os = os;
        this.inputExchanger = inputExchanger;
        this.outputExchanger = outputExchanger;
    }
    public abstract void handle(byte headerByte) throws IOException;

}
