package com.t13max.fight.event;

import battle.entity.FightEventPb;
import battle.event.entity.BuffUpdateEventPb;
import com.t13max.fight.buff.IBuffBox;
import lombok.Getter;

/**
 * @author: t13max
 * @since: 11:50 2024/5/27
 */
@Getter
public class BuffUpdateEvent extends AbstractEvent implements IFightEventPackager {

    private final IBuffBox buffBox;

    public BuffUpdateEvent(IBuffBox buffBox) {
        super(FightEventEnum.BUFF_UPDATE);
        this.buffBox = buffBox;
    }

    @Override
    public FightEventPb pack() {
        FightEventPb.Builder builder = FightEventPb.newBuilder();
        BuffUpdateEventPb.Builder eventBuilder = BuffUpdateEventPb.newBuilder();
        eventBuilder.setBuffBox(buffBox.pack());
        builder.setBuffUpdateEventPb(eventBuilder);
        return builder.build();
    }
}
