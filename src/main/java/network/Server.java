package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {
    private ServerSocket serverSocket;
    private ThreadPoolExecutor threadPoolExecutor;
    public Server() throws IOException {
        this.serverSocket = new ServerSocket(80);
        this.threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void listen() throws IOException {
        while (true) {
            Socket socket = this.serverSocket.accept();
            threadPoolExecutor.execute(new ConnectionHandler(socket));
        }
    }
}
