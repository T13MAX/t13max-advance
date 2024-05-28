package com.t13max.fight.event;

import com.t13max.fight.buff.effect.IBuffEffect;
import lombok.Getter;

/**
 * @author: t13max
 * @since: 11:35 2024/4/11
 */
@Getter
public class BuffEffectCanActiveEvent extends AbstractEvent {

    private IBuffEffect buffEffect;

    public BuffEffectCanActiveEvent(IBuffEffect buffEffect) {
        super(FightEventEnum.BUFF_EFFECT_CAN_ACTIVE);
        this.buffEffect = buffEffect;
    }
}
