package com.t13max.fight.event;

/**
 * @author: t13max
 * @since: 16:03 2024/4/15
 */
public class BeforeActionEvent extends AbstractEvent {

    public BeforeActionEvent() {
        super(FightEventEnum.BEFORE_ACTION);
    }

}
