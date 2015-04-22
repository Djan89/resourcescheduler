package com.mt.jpmorgan.resourcescheduler;

import com.mt.jpmorgan.resourcescheduler.external.Gateway;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;

/**
 * Scheduler responsible for managing the interaction with an expensive external resource.
 * The Schedule manage the interaction based on the availability of the external resource.
 * It can be configured with the number of resources, as resources became available it sends the
 * messages to the Gateway
 */
public class Scheduler {

    final static Logger logger = Logger.getLogger(Scheduler.class);

    private static final Scheduler instance = new Scheduler();
    
    private static Gateway simpleGateway = null;
    private static SchedulerPriority schedulerPriority = null;
    
    private static int maxExternalServiceSize = 1;
    private static int activeExternalService = 0;
    private static SchedulerStatus status = SchedulerStatus.STOPED;
    
    private final InnerCallback callback = new InnerCallback(); 

    private static ExecutorService taskExecutor = null;
    Queue<Future> futures = new ConcurrentLinkedDeque<>();
    
    private Scheduler() {
        // Singleton
    }

    /**
     * Get an instance of scheduler.
     * 
     * @param externalServiceSize number of resources available
     * @param prioritization priorization algorithm
     * @param gateway where to send the messages to.
     * @return an instance of scheduler
     */
    public static Scheduler getInstance(int externalServiceSize, SchedulerPriority prioritization, Gateway gateway) {
        if (externalServiceSize < 1 || prioritization == null || gateway == null) {
            throw new IllegalArgumentException();
        }
        maxExternalServiceSize = externalServiceSize;
        taskExecutor = Executors.newFixedThreadPool(externalServiceSize + 1); // extra thread for the process method
        schedulerPriority = prioritization;
        simpleGateway = gateway;
        return instance;
    }

    /**
     * Schedule messages to be sent to the gateway as resource became available.
     * Messages belonging to a canceled group will not be scheduled. 
     * 
     * @param msg message to send.
     */
    public void schedule(SchedulerMessage msg) {      
        schedulerPriority.add(msg);
        logger.info(String.format("Message from Group %d Scheduled!", msg.getGroupId()));
        if (SchedulerStatus.STOPED == status) {
            status = SchedulerStatus.RUNNING;
            futures.add(
                taskExecutor.submit(
                    new Runnable() {
                        @Override
                        public void run() {
                            process();
                        }
                    }
                )
            );
        }        
    }

    /**
     * Prevent a group of messages of been sent to the gateway.
     * 
     * @param groupId messages group
     */
    public void cancelGroup(int groupId){
        schedulerPriority.cancelGroup(groupId);
        logger.info(String.format("Messages from Group %d Cancelled!", groupId));
    }
    
    /**
     * Shutdown the scheduler when all tasks as been executed.
     */
    public void close() {
        try {
            for (Future f : futures) { // Wait for all the tasks to finish.
                f.get();
            }
            logger.info("No more Messages to be executed! Shutting Down...");
            taskExecutor.shutdown();
        } catch (InterruptedException | ExecutionException ex) {
            taskExecutor.shutdownNow();
        }
    }
    
    private void process() {
        try {
            while (schedulerPriority.hasNextMessage()) {
                if (activeExternalService < maxExternalServiceSize) {
                    final SchedulerMessage nextMessage = schedulerPriority.get();
                    if (nextMessage != null) {
                        nextMessage.setCallback(callback);
                        futures.add(
                            taskExecutor.submit(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        simpleGateway.send(nextMessage);
                                    }
                                }
                            )
                        );
                        activeExternalService++;                        
                        logger.info(String.format(" ---> Message from Group %d Sent!", nextMessage.getGroupId()));
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            status = SchedulerStatus.STOPED;
        }
    }

    class InnerCallback implements Callback{
        @Override
        public void onRequestComplete(int groupId) {
            activeExternalService--;
            schedulerPriority.processed(groupId);
            logger.info(String.format("Message from Group %d Procesed!", groupId));
        }
    }
}
