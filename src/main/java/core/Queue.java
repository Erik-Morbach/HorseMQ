package core;

import java.nio.Buffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Queue {
    private String producer;
    private Set<String> consumers;
    private LinkedList<Double> messages = new LinkedList<>();

    public Queue(String producer, Set<String> consumers){
        this.producer = producer;
        this.consumers = consumers;
    }

    public void enqueue(Double msg) {
        messages.addLast(msg);
    }

    public Double dequeue() {
        return messages.isEmpty() ? null : messages.removeFirst();
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public int size() {
        return messages.size();
    }

    public Iterator<Double> iterator() {
        return messages.iterator();
    }

    public boolean addConsumer(String consumerId){
        return consumers.add(consumerId);
    }

    public String getProducer(){
        return producer;
    }

    public Set<String> getConsumers(){
        return this.consumers;
    }
}
