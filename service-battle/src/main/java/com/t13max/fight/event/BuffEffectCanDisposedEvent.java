package com.t13max.fight.event;

import com.t13max.fight.buff.RemoveReason;
import com.t13max.fight.buff.effect.IBuffEffect;
import lombok.Getter;

/**
 * @author: t13max
 * @since: 11:39 2024/4/11
 */
@Getter
public class BuffEffectCanDisposedEvent extends AbstractEvent {

    private IBuffEffect buffEffect;

    private RemoveReason removeReason;

    public BuffEffectCanDisposedEvent(IBuffEffect buffEffect, RemoveReason removeReason) {
        super(FightEventEnum.BUFF_EFFECT_CAN_DISPOSED);
        this.buffEffect = buffEffect;
        this.removeReason = removeReason;
    }
}
