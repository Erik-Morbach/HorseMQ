package network.operations;

import java.net.Socket;

public class Create extends Operation {
    public Create(Socket socket) {
        super(socket);
    }

    @Override
    public void handle(byte headerByte) {

    }

}
