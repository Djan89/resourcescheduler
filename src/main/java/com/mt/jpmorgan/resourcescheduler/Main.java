package com.mt.jpmorgan.resourcescheduler;

/**
 * Integration tests.
 */
public class Main {
    
    public static void main(String[] args) {
        sendMessages(1, new DefaultSchedulerPriority());        
        sendMessages(2, new DefaultSchedulerPriority());   
        
        sendMessages(1, new AlternativeSchedulerPriority());
        sendMessages(2, new AlternativeSchedulerPriority());

        sendMessagesAndCancelGroup(1, new DefaultSchedulerPriority());
        sendMessagesAndCancelGroup(2, new DefaultSchedulerPriority());
        
        sendMessagesAndCancelGroup(1, new AlternativeSchedulerPriority());
        sendMessagesAndCancelGroup(2, new AlternativeSchedulerPriority());    
    }
    
    public static void sendMessages(int resouceAvailable, SchedulerPriority schedulerPriority){
        System.out.println(String.format("--- SendMessages Test with %d resource(s) and using %s ---", 
                resouceAvailable, schedulerPriority.getClass().getSimpleName()));
        
        Scheduler scheduler = Scheduler.getInstance(resouceAvailable, schedulerPriority, new SimpleGateway());
        
        SchedulerMessage msg = new SimpleMessage(1);
        scheduler.schedule(msg);
        
        SchedulerMessage msg1 = new SimpleMessage(2);
        scheduler.schedule(msg1);
        
        SchedulerMessage msg2 = new SimpleMessage(3);
        scheduler.schedule(msg2);
        
        SchedulerMessage msg3 = new SimpleMessage(1);
        scheduler.schedule(msg3);
        
        SchedulerMessage msg4 = new SimpleMessage(1);
        scheduler.schedule(msg4);
        
        scheduler.close();
    } 
    
    public static void sendMessagesAndCancelGroup(int resouceAvailable, SchedulerPriority schedulerPriority) {
        System.out.println(String.format("--- SendMessagesAndCancelGroup Test with %d resource(s) and using %s ---",
                resouceAvailable, schedulerPriority.getClass().getSimpleName()));
        
        Scheduler scheduler = Scheduler.getInstance(resouceAvailable, schedulerPriority, new SimpleGateway());
        
        SchedulerMessage msg = new SimpleMessage(1);
        scheduler.schedule(msg);
        
        SchedulerMessage msg1 = new SimpleMessage(2);
        scheduler.schedule(msg1);
        
        SchedulerMessage msg2 = new SimpleMessage(3);
        scheduler.schedule(msg2);
        
        SchedulerMessage msg3 = new SimpleMessage(1);
        scheduler.schedule(msg3);
        scheduler.cancelGroup(msg3.getGroupId());
        
        SchedulerMessage msg4 = new SimpleMessage(1);
        scheduler.schedule(msg4);
        
        scheduler.close();
    }   

}
