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
public class SchedulerTest {
    
    /**
     * Test of onRequestComplete method, of class Scheduler.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetInstance() {
        Assert.assertNotNull(Scheduler.getInstance(2, new DefaultSchedulerPriority(), new SimpleGateway()));
        
        Scheduler.getInstance(0, null, null);
    }
    
    @Test
    public void testSchedule() {
        System.out.println("--- Schedule Test ---");
        Scheduler scheduler = Scheduler.getInstance(2, new DefaultSchedulerPriority(), new SimpleGateway());
        SchedulerMessage msg1 = new SimpleMessage(1);
        scheduler.schedule(msg1);

        SchedulerMessage msg2 = new SimpleMessage(2);
        scheduler.schedule(msg2);

        SchedulerMessage msg3 = new SimpleMessage(3);
        scheduler.schedule(msg3);

        SchedulerMessage msg4 = new SimpleMessage(1);
        scheduler.schedule(msg4);

        SchedulerMessage msg5 = new SimpleMessage(1);
        scheduler.schedule(msg5);

        scheduler.close();
    }
    
    @Test
    public void testCancelGroup() {
        System.out.println("--- CancelGroup Test ---");
        Scheduler scheduler = Scheduler.getInstance(1, new DefaultSchedulerPriority(), new SimpleGateway());
        SchedulerMessage msg1 = new SimpleMessage(1);
        scheduler.schedule(msg1);
        
        SchedulerMessage msg2 = new SimpleMessage(2);
        scheduler.schedule(msg2);
        
        SchedulerMessage msg3 = new SimpleMessage(3);
        scheduler.schedule(msg3);
        
        SchedulerMessage msg4 = new SimpleMessage(1);
        scheduler.schedule(msg4);
        
        scheduler.cancelGroup(msg4.getGroupId());
        
        SchedulerMessage msg5 = new SimpleMessage(1);
        scheduler.schedule(msg5);
        
        scheduler.close();
    }
}
