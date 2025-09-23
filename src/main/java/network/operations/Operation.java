package network.operations;

import java.net.Socket;

public abstract class Operation {
    protected Socket socket;
    public Operation(Socket socket){
        this.socket = socket;
    }
    public abstract void handle(byte headerByte);
}
