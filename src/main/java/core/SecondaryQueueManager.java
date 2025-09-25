package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SecondaryQueueManager {
    private String consumerName;
    private Iterator<DoubleMessage> pointer;

    public SecondaryQueueManager(String consumerName, Queue primaryQueue) {
        this.consumerName = consumerName;
        this.pointer = primaryQueue.iterator();
    }

    public List<Double> dispatchToConsumer(int maxMessages) {
        List<Double> result = new ArrayList<>();
        int count = 0;

        while (pointer.hasNext() && count < maxMessages) {
            DoubleMessage doubleMessage = pointer.next();
            doubleMessage.accesses++;
            result.addAll(doubleMessage.values);
            count++;
        }

        return result;
    }
}
