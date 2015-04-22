package com.mt.jpmorgan.resourcescheduler.external;

/**
 * Gateway API to external resource.
 */
public interface Gateway {
    void send(Message msg);
}
