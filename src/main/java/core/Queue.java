package core;

import java.nio.Buffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Queue {
    private String producer;
    private Set<String> consumers;
    private LinkedList<DoubleMessage> messages = new LinkedList<>();


    public Queue(String producer, Set<String> consumers){
        this.producer = producer;
        this.consumers = consumers;;
    }

    public void enqueue(DoubleMessage msg) {
        messages.addLast(msg);
    }

    public DoubleMessage dequeue() {
        return messages.isEmpty() ? null : messages.removeFirst();
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public int size() {
        return messages.size();
    }

    public Iterator<DoubleMessage> iterator() {
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

    public List<DoubleMessage> getMessages(){
        return this.messages;
    }
}
