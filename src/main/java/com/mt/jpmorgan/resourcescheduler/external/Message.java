package com.mt.jpmorgan.resourcescheduler.external;

/**
 * External Message API.
 */
public interface Message {
    /**
     * Method executed when message processing is completed.
     */
    void completed();
    
    /**
     * Return the message group id.
     */
    int getGroupId();
}
