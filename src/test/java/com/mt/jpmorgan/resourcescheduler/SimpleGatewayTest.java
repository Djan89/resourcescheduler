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
public class SimpleGatewayTest {
    
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
    public void testSend() {
        SchedulerMessage msg = new SimpleMessage(1);
        msg.setCallback(new TestCallback());
        SimpleGateway simpleGateway = new SimpleGateway();
        simpleGateway.send(msg);
        Assert.assertTrue(callbackCalled);
    }
    
}
