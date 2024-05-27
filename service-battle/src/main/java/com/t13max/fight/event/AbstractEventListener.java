package com.t13max.fight.event;

import java.util.*;

/**
 * @author: t13max
 * @since: 18:05 2024/4/15
 */
public abstract class AbstractEventListener implements IFightEventListener {

    private Set<FightEventEnum> interestedEvent = new HashSet<>();

    protected void subscribeEvent(List<FightEventEnum> enums) {
        if (enums == null || enums.isEmpty()) {
            return;
        }
        interestedEvent.addAll(enums);
    }

    protected void subscribeEvent(FightEventEnum... enums) {
        if (enums == null || enums.length == 0) {
            return;
        }
        Collections.addAll(interestedEvent, enums);
    }

    @Override
    public Set<FightEventEnum> getInterestedEvent() {
        return this.interestedEvent;
    }

}
