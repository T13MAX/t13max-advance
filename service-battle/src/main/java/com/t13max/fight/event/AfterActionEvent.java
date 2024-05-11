package com.t13max.fight.event;

/**
 * @author: t13max
 * @since: 16:12 2024/4/15
 */
public class AfterActionEvent extends AbstractEvent {

    public AfterActionEvent() {
        super(FightEventEnum.AFTER_ACTION);
    }
}
