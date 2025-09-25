package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SecondaryQueueManager {
    private String consumerName;
    private Queue primaryQueue;
    private Iterator<Double> pointer;

    public SecondaryQueueManager(String consumerName, Queue primaryQueue) {
        this.consumerName = consumerName;
        this.primaryQueue = primaryQueue;
        this.pointer = primaryQueue.iterator();
    }

    public List<Double> dispatchToConsumer(int maxMessages) {
        List<Double> result = new ArrayList<>();
        int count = 0;

        while (pointer.hasNext() && count < maxMessages) {
            result.add(pointer.next());
            count++;
        }

        return result;
    }

    public String getConsumerName() {
        return consumerName;
    }
}
