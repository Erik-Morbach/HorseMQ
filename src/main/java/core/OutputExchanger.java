package core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutputExchanger {
    private final PrimaryQueueManager primaryQueueManager;
    private final Map<String, SecondaryQueueManager> secondaryQueueManagerMap;

    public OutputExchanger(SecondaryQueueManager secondaryQueueManager, PrimaryQueueManager primaryQueueManager, Map<String, SecondaryQueueManager> secondaryQueueManagerMap) {
        this.primaryQueueManager = primaryQueueManager;
        this.secondaryQueueManagerMap = new HashMap<>();
    }

    public boolean firstQueueConnection(String consumerId, String queueId){
        var mainQueue = primaryQueueManager.findQueueByQueueId(queueId);

        if (mainQueue == null) return false;

        if (!mainQueue.getConsumers().contains(consumerId)) return false;

        var secQueueManager = new SecondaryQueueManager(consumerId, mainQueue);
        secQueueManager = secondaryQueueManagerMap.putIfAbsent((consumerId + queueId), secQueueManager);
        return secQueueManager != null;
    }

    public List<Double> sendMessage(int maxMessages, String consumerId) {
        SecondaryQueueManager secQueueManager = secondaryQueueManagerMap.get(consumerId);
        if(secQueueManager == null) throw new RuntimeException("consumer does not have a queue");
        return secQueueManager.dispatchToConsumer(maxMessages);
    }
}
