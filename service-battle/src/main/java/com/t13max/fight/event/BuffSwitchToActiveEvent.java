package com.t13max.fight.event;

import battle.entity.FightEventPb;
import battle.event.entity.BuffActionEventPb;
import com.t13max.fight.buff.BuffBoxImpl;
import lombok.Getter;

/**
 * @author: t13max
 * @since: 16:42 2024/5/27
 */
@Getter
public class BuffSwitchToActiveEvent extends AbstractEvent implements IFightEventPackager {

    private BuffBoxImpl buffBox;

    public BuffSwitchToActiveEvent(BuffBoxImpl buffBox) {
        super(FightEventEnum.BUFF_SWITCH_TO_ACTIVE);
        this.buffBox = buffBox;
    }

    @Override
    public FightEventPb pack() {
        FightEventPb.Builder builder = FightEventPb.newBuilder();
        BuffActionEventPb.Builder eventBuilder = BuffActionEventPb.newBuilder();
        eventBuilder.setBuffStatus(buffBox.getBuffStatus());
        builder.setBuffActionEventPb(eventBuilder);
        return builder.build();
    }
}
