package com.t13max.fight.trigger;

import java.util.*;

/**
 * @author: t13max
 * @since: 17:00 2024/4/15
 */
public class TriggerTimeline {

    private Map<Integer, LinkedList<ITrigger>> triggerListMap = new HashMap<>();

    public boolean hasTriggerRemains() {
        return !triggerListMap.isEmpty();
    }

    public boolean add(ITrigger trigger) {
        LinkedList<ITrigger> triggers = triggerListMap.computeIfAbsent(trigger.getDelayTime(), k -> new LinkedList<>());
        return triggers.add(trigger);
    }

    public boolean remove(ITrigger trigger) {
        LinkedList<ITrigger> triggers = triggerListMap.get(trigger.getDelayTime());
        if (triggers == null) {
            return false;
        }
        return triggers.remove(trigger);
    }

    public Collection<ITrigger> getNextTriggerList() {
        Optional<Integer> nextTimeOptional = triggerListMap.keySet().stream().min(Integer::compare);
        if (nextTimeOptional.isEmpty()) {
            return Collections.emptyList();
        }

        return triggerListMap.remove(nextTimeOptional.get());
    }
}
