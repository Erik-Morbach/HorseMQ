package core;

public abstract class QueueManager {
    public abstract boolean handleMessage(String queueId, String producerId, Double msg);
}
