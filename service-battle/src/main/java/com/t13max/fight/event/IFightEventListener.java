package com.t13max.fight.event;

import java.util.Set;

/**
 * @author: t13max
 * @since: 15:15 2024/4/10
 */
public interface IFightEventListener {

    Set<FightEventEnum> getInterestedEvent();

    void onEvent(IFightEvent event);

    default int getPriority() {
        return 0;
    }
}
