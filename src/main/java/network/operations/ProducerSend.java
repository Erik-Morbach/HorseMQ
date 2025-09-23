package network.operations;

import java.net.Socket;

public class ProducerSend extends Operation {
    public ProducerSend(Socket socket) {
        super(socket);
    }

    @Override
    public void handle(byte headerByte) {

    }
}
