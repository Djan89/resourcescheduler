package com.mt.jpmorgan.resourcescheduler;

/**
 * Scheduler Priorization API.
 */
public interface SchedulerPriority {

    void add(SchedulerMessage msg);

    SchedulerMessage get();

    boolean hasNextMessage();
    
    void processed(int groupID);
    
    void cancelGroup(int groupID);
}
