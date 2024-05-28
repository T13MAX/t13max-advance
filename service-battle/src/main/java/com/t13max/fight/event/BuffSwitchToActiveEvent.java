package com.t13max.fight.event;

import com.t13max.fight.buff.BuffBoxImpl;
import lombok.Getter;

/**
 * @author: t13max
 * @since: 16:42 2024/5/27
 */
@Getter
public class BuffSwitchToActiveEvent extends AbstractEvent {

    private BuffBoxImpl buffBox;

    public BuffSwitchToActiveEvent(BuffBoxImpl buffBox) {
        super(FightEventEnum.BUFF_SWITCH_TO_ACTIVE);
        this.buffBox = buffBox;
    }
}
