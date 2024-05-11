package com.t13max.fight.event;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author: t13max
 * @since: 18:05 2024/4/15
 */
public abstract class AbstractEventListener implements IFightEventListener {

    private Set<FightEventEnum> interestedEvent = new HashSet<>();

    protected void subscribeEvent(FightEventEnum... enums) {
        if (Objects.nonNull(enums)) {
            if (enums.length > 0) {
                Collections.addAll(interestedEvent, enums);
            }
        }
    }

    @Override
    public Set<FightEventEnum> getInterestedEvent() {
        return this.interestedEvent;
    }

}
