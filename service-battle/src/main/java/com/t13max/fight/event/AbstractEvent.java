package com.t13max.fight.event;

/**
 * @author: t13max
 * @since: 17:14 2024/4/15
 */
public class AbstractEvent implements IFightEvent {

    private FightEventEnum eventEnum;

    public AbstractEvent(FightEventEnum eventEnum) {
        this.eventEnum = eventEnum;
    }

    @Override
    public FightEventEnum getFightEventEnum() {
        return this.eventEnum;
    }
}
