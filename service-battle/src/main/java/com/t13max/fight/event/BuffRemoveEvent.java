package com.t13max.fight.event;

import battle.entity.FightEventPb;
import battle.event.entity.BuffRemoveEventPb;
import battle.event.entity.RemoveReason;
import com.t13max.fight.buff.IBuffBox;
import lombok.Getter;

/**
 * buff从宿主身上移除
 *
 * @Author t13max
 * @Date 11:24 2024/5/28
 */
@Getter
public class BuffRemoveEvent extends AbstractEvent implements IFightEventPackager {

    private final IBuffBox buff;
    private final RemoveReason removeReason;

    public BuffRemoveEvent(IBuffBox buff, RemoveReason removeReason) {
        super(FightEventEnum.BUFF_REMOVE_FROM_HOST);
        this.buff = buff;
        this.removeReason = removeReason;
    }

    @Override
    public FightEventPb pack() {
        FightEventPb.Builder builder = FightEventPb.newBuilder();
        BuffRemoveEventPb.Builder eventBuilder = BuffRemoveEventPb.newBuilder();
        eventBuilder.setBuffStatus(buff.getBuffStatus());
        eventBuilder.setReason(removeReason);
        builder.setBuffRemoveEventPb(eventBuilder);
        return builder.build();
    }
}
