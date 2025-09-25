package core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InputExchanger {
    PrimaryQueueManager queueManager;

    public InputExchanger(PrimaryQueueManager primaryQueueManager){
        this.queueManager = primaryQueueManager;
    }

    public boolean registerQueue(String queueId, String producerId, Set<String> consumersIds) {
        return queueManager.createQueue(queueId, producerId, consumersIds);
    }

    public void receiveMessage(String queueId, String producerId, DoubleMessage msg) {
        queueManager.handleMessage(queueId, producerId, msg);
    }

    public boolean addConsumerToQueue(String queueId, String consumerId){
        return queueManager.addConsumerToQueue(queueId, consumerId);
    }

    public boolean registerConsumer(String consumerId){
        return queueManager.addConsumerToBroker(consumerId);
    }
}
