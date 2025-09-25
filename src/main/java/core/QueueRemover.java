package core;

import java.util.Iterator;
import java.util.List;

public class QueueRemover implements Runnable{
    private List<Queue> queues;

    public QueueRemover(List<Queue> queue){
        this.queues = queues;
    }

    public void run() {
        while (true) {
            try {
                if (queues == null) {
                    Thread.sleep(1000);
                    continue;
                }
                queues.forEach(queue -> {
                    Iterator<DoubleMessage> it = queue.getMessages().iterator();
                    while (it.hasNext()) {
                        DoubleMessage message = it.next();
                        if (message.accesses == queue.getConsumers().size()) {
                            it.remove();
                        }
                        if (message.accesses == 0) break;
                    }
                });
            } catch (InterruptedException e){
                System.out.println("error in thread.");
            }
        }
    }
}
