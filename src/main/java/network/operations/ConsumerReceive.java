package network.operations;

import java.net.Socket;

public class ConsumerReceive extends Operation {
    public ConsumerReceive(Socket socket) {
        super(socket);
    }

    @Override
    public void handle(byte headerByte) {

    }
}
