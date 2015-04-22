package com.mt.jpmorgan.resourcescheduler;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Default Scheduler priorization algorithm with the following requirements:
 *  -> Messages to the Gateway have a logical grouping
 *  -> Messages are not guaranteed to be delivered in their groups. I.E. you might get messages from group2 before you are finished with group1
 *  -> Where possible, the message groups should not be interleaved...except where resources are idle and other work can be done. 
 *  -> The priority in which to process groups is defined by the order in which you receive the first message from the group
 *  -> If there are messages belonging to multiple groups in the queue, as resources become available, return messages from groups already started.
 */
public class DefaultSchedulerPriority implements SchedulerPriority {

    protected final Map<String, Queue<SchedulerMessage>> messages = new ConcurrentHashMap<>();
    protected final Queue<String> messageGroupOrder = new ConcurrentLinkedDeque<>();
    protected final Queue<String> cancelledGroups = new ConcurrentLinkedDeque<>();

    @Override
    public void add(SchedulerMessage msg) {
        String key = String.valueOf(msg.getGroupId());
        if (!cancelledGroups.contains(key)) {
            if (messages.containsKey(key)) {
                messages.get(key).add(msg);
            } else {
                Queue<SchedulerMessage> message = new ConcurrentLinkedDeque<>();
                message.add(msg);
                messages.put(key, message);
                messageGroupOrder.add(key);
            }
        }
    }

    @Override
    public SchedulerMessage get() {
        SchedulerMessage msg = null;
        if (!messages.isEmpty() && !messageGroupOrder.isEmpty()) {
            final Iterator<String> iterator = messageGroupOrder.iterator();
            while (iterator.hasNext()) {
                Queue<SchedulerMessage> messagesOfGroup = messages.get(iterator.next());
                if (messagesOfGroup != null && !messagesOfGroup.isEmpty()) {
                    msg = messagesOfGroup.poll();
                    break;
                }
            }
        }
        return msg;
    }

    @Override
    public boolean hasNextMessage() {
        return (!messages.isEmpty() && !messageGroupOrder.isEmpty());
    }

    @Override
    public void processed(int groupID) {
        final String key = String.valueOf(groupID);
        Queue<SchedulerMessage> messagesOfGroup = messages.get(key);
        if (messagesOfGroup != null && messagesOfGroup.isEmpty()) {
            messages.remove(key);
            messageGroupOrder.remove(key);
        }
    }

    @Override
    public void cancelGroup(int groupID) {
        final String key = String.valueOf(groupID);
        messages.remove(key);
        messageGroupOrder.remove(key);
        cancelledGroups.add(key);
    }
}
