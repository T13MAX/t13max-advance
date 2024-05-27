package com.t13max.fight.event;

import com.t13max.fight.buff.IBuffBox;
import lombok.Getter;

/**
 * @author: t13max
 * @since: 11:50 2024/5/27
 */
@Getter
public class BuffUpdateEvent extends AbstractEvent {

    private IBuffBox buffBox;

    public BuffUpdateEvent(IBuffBox buffBox) {
        super(FightEventEnum.BUFF_UPDATE);
        this.buffBox = buffBox;
    }
}
