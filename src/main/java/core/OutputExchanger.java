package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutputExchanger {
    private final PrimaryQueueManager primaryQueueManager;
    private final Map<String, SecondaryQueueManager> secondaryQueueManagerMap;

    public OutputExchanger(PrimaryQueueManager primaryQueueManager) {
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

    public List<DoubleMessage> sendMessage(int maxMessages, String consumerId, String queueId) {
        SecondaryQueueManager secQueueManager = secondaryQueueManagerMap.get(consumerId + queueId);
        if(secQueueManager == null) return new ArrayList<>();
        return secQueueManager.dispatchToConsumer(maxMessages);
    }
}
