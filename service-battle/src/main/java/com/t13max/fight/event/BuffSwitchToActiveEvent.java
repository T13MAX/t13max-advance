package com.t13max.fight.event;

import com.t13max.fight.buff.BuffBoxImpl;

/**
 * @author: t13max
 * @since: 16:42 2024/5/27
 */
public class BuffSwitchToActiveEvent extends AbstractEvent {

    private BuffBoxImpl buffBox;

    public BuffSwitchToActiveEvent(BuffBoxImpl buffBox) {
        super(FightEventEnum.BUFF_SWITCH_TO_ACTIVE);
        this.buffBox = buffBox;
    }
}
