/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mt.jpmorgan.resourcescheduler;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mamadjan Teixeira
 */
public class AlternativeSchedulerPriorityTest {
    
    /**
     * Test of add method, of class DefaultSchedulerPriority.
     */
    @Test
    public void testAdd() {
        SchedulerPriority sp = new DefaultSchedulerPriority();
        Assert.assertFalse(sp.hasNextMessage());

        sp.add(new SimpleMessage(1));
        Assert.assertTrue(sp.hasNextMessage());
    }

    /**
     * Test of get method, of class DefaultSchedulerPriority.
     */
    @Test
    public void testGet() {
        SchedulerPriority sp = new AlternativeSchedulerPriority();
        Assert.assertNull(sp.get());

        sp.add(new SimpleMessage(1));
        Assert.assertNotNull(sp.get());
    }

    @Test
    public void testPriorization() {
        SchedulerPriority sp = new AlternativeSchedulerPriority();
        Assert.assertNull(sp.get());

        final SimpleMessage msg1 = new SimpleMessage(1);
        sp.add(msg1);
        Assert.assertEquals(msg1, sp.get());

        final SimpleMessage msg2 = new SimpleMessage(2);
        sp.add(msg2);

        final SimpleMessage msg3 = new SimpleMessage(3);
        sp.add(msg3);

        sp.add(msg2);

        Assert.assertEquals(msg2, sp.get());
        Assert.assertEquals(msg3, sp.get());
        Assert.assertEquals(msg2, sp.get());
    }

    /**
     * Test of hasNextMessage method, of class DefaultSchedulerPriority.
     */
    @Test
    public void testHasNextMessage() {
        SchedulerPriority sp = new AlternativeSchedulerPriority();
        Assert.assertFalse(sp.hasNextMessage());

        sp.add(new SimpleMessage(1));
        Assert.assertTrue(sp.hasNextMessage());
    }

}
