package com.t13max.fight.event;

import battle.entity.FightEventPb;
import battle.event.entity.BuffAddEventPb;
import com.t13max.fight.buff.IBuffBox;
import lombok.Getter;

/**
 * buff添加到玩家身上
 *
 * @Author t13max
 * @Date 11:23 2024/5/28
 */
@Getter
public class BuffAddEvent extends AbstractEvent implements IFightEventPackager {

    private final IBuffBox buff;

    public BuffAddEvent(IBuffBox buff) {
        super(FightEventEnum.BUFF_ADD_TO_HOST);
        this.buff = buff;
    }

    @Override
    public FightEventPb pack() {
        FightEventPb.Builder builder = FightEventPb.newBuilder();
        BuffAddEventPb.Builder eventBuilder = BuffAddEventPb.newBuilder();
        eventBuilder.setBuffBox(buff.pack());
        builder.setBuffAddEventPb(eventBuilder);
        return builder.build();
    }
}
