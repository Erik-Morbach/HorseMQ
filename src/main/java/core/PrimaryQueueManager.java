package core;

import java.util.*;

public class PrimaryQueueManager extends QueueManager {
    private final Map<String, Queue> queues = new HashMap<>();
    private final Set<String> consumers = new HashSet<>();
    private final Set<String> producers = new HashSet<>();

    public boolean createQueue(String queueId, String producerId, Set<String> consumerIds){
        Queue queue = queues.putIfAbsent(queueId, new Queue(producerId, consumerIds));
        return (queue != null);
    }

    @Override
    public boolean handleMessage(String queueId, String producerId, DoubleMessage msg) {
        Queue queue = queues.get(queueId);

        if (queue == null) return false;

        if (!queue.getProducer().equals(producerId)) return false;

        queue.enqueue(msg);

        return true;
    }

    public boolean addConsumerToQueue(String queueId, String consumerId){
        Queue queue = queues.get(queueId);
        if (queue == null) {
            return false;
        }
        return queue.addConsumer(consumerId);
    }

    public boolean addConsumerToBroker(String consumerId){
        return consumers.add(consumerId);
    }
    public boolean addProducerToBroker(String producerId){
        return producers.add(producerId);
    }

    public Queue findQueueByQueueId(String queueId){
        return queues.get(queueId);
    }
}
