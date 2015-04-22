/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mt.jpmorgan.resourcescheduler;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Mamadjan Teixeira
 */
public class SimpleMessageTest {

    private boolean callbackCalled = false;

    class TestCallback implements Callback {
        @Override
        public void onRequestComplete(int groupId) {
            callbackCalled = true;
        }
    }

    /**
     * Test of send method, of class SimpleGateway.
     */
    @Test
    public void testCallbackExecutedOnCompleted() {
        SchedulerMessage msg = new SimpleMessage(1);
        msg.setCallback(new TestCallback());
        msg.completed();
        Assert.assertTrue(callbackCalled);
    }

}
