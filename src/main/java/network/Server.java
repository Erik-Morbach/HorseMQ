package network;

import core.InputExchanger;
import core.OutputExchanger;
import core.PrimaryQueueManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {
    private ServerSocket serverSocket;
    private ThreadPoolExecutor threadPoolExecutor;
    InputExchanger inputExchanger;
    OutputExchanger outputExchanger;
    PrimaryQueueManager primaryQueueManager;
    public Server() throws IOException {
        this.serverSocket = new ServerSocket(80);
        this.threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.primaryQueueManager = new PrimaryQueueManager();
        this.inputExchanger = new InputExchanger(primaryQueueManager);
        this.outputExchanger = new OutputExchanger(primaryQueueManager);
    }

    public void listen() throws IOException {

        while (true) {
            Socket socket = this.serverSocket.accept();
            threadPoolExecutor.execute(new ConnectionHandler(socket, inputExchanger, outputExchanger));
        }
    }
}
