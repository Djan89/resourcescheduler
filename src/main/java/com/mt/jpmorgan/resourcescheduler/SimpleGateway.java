package com.mt.jpmorgan.resourcescheduler;

import com.mt.jpmorgan.resourcescheduler.external.Gateway;
import com.mt.jpmorgan.resourcescheduler.external.Message;
import org.apache.log4j.Logger;

/**
 * Simple gateway implementation.
 */
public class SimpleGateway implements Gateway{    
    final static Logger logger = Logger.getLogger(SimpleGateway.class);
    
    @Override
    public void send(final Message msg) {     
        externalResource(msg);
    }

    private static void externalResource(final Message msg) {
        try {
            Thread.sleep(1000); // For simulating a very time consuming operation.
        } catch (InterruptedException ex) {
            logger.error(ex);
        }
        msg.completed();
    }
    
}

