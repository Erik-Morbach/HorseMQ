package network.operations;

import java.net.Socket;

public class ConsumerRegistration extends Operation {
    public ConsumerRegistration(Socket socket) {
        super(socket);
    }

    @Override
    public void handle(byte headerByte) {

    }
}
