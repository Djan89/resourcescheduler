package com.mt.jpmorgan.resourcescheduler;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Alternative Scheduler priorization algorithm with the following requirements: 
 * -> Process messages as they arrive in a FIFO order. 
 */
public class AlternativeSchedulerPriority implements SchedulerPriority {

    protected static final Queue<SchedulerMessage> messages = new ConcurrentLinkedDeque<>();
    
    @Override
    public void add(SchedulerMessage msg) {
       messages.add(msg);
    }

    @Override
    public SchedulerMessage get() {
        return messages.poll();
    }

    @Override
    public boolean hasNextMessage() {
        return !messages.isEmpty();
    }

    @Override
    public void processed(int groupID) {
        // Does nothing.
    }

    @Override
    public void cancelGroup(int groupID) {
        // Operation not supported.
    }
    
}
