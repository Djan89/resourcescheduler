package com.mt.jpmorgan.resourcescheduler;

/**
 * Callback API.
 */
public interface Callback {
    void onRequestComplete(int groupId);
}
