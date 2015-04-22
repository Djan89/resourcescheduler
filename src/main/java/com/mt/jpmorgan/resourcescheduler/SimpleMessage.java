package com.mt.jpmorgan.resourcescheduler;

import org.apache.log4j.Logger;

/**
 * Simple implementation of {@link SchedulerMessage}. 
 */
public class SimpleMessage implements SchedulerMessage {

    final static Logger logger = Logger.getLogger(SimpleMessage.class);
       
    private final int groupId;
    private Callback callback;

    public SimpleMessage(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public int getGroupId() {
        return this.groupId;
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public Callback getCallback() {
        return this.callback;
    }
    
    @Override
    public void completed() {
        if(getCallback() != null){
            getCallback().onRequestComplete(getGroupId());
        }
    }
    
}
