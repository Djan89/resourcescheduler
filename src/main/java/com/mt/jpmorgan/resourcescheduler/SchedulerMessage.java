package com.mt.jpmorgan.resourcescheduler;

import com.mt.jpmorgan.resourcescheduler.external.Message;

/**
 * Scheduler message that extend {@link Message}.
 */
public interface SchedulerMessage extends Message{
    void setCallback(Callback callback);
    Callback getCallback();
}
