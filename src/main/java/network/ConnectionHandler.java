package network;

import network.operations.ConsumerReceive;
import network.operations.ConsumerRegistration;
import network.operations.Create;
import network.operations.Operation;
import network.operations.ProducerSend;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.function.Function;

public class ConnectionHandler implements Runnable {
    private Socket socket;
    private final List<Function<Socket, Operation>> operations = List.of(
            Create::new,
            ConsumerRegistration::new,
            ProducerSend::new,
            ConsumerReceive::new
    );
    public ConnectionHandler(Socket socket) throws IOException {
        this.socket = socket;
    }
    @Override
    public void run() {
        try(BufferedInputStream is = new BufferedInputStream(socket.getInputStream())){
            byte header = is.readNBytes(1)[0];
            byte operation = (byte) (header >> 6);
            if(operation > 3) return;
            operations.get(operation)
                    .apply(socket)
                    .handle(header);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
