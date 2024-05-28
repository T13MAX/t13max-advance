package com.t13max.fight.event;

import com.t13max.fight.buff.IBuffBox;
import com.t13max.fight.buff.RemoveReason;
import lombok.Getter;

/**
 * buff从宿主身上移除
 *
 * @Author t13max
 * @Date 11:24 2024/5/28
 */
@Getter
public class BuffRemoveFromHostEvent extends AbstractEvent {

    private final IBuffBox buff;
    private final RemoveReason removeReason;

    public BuffRemoveFromHostEvent(IBuffBox buff, RemoveReason removeReason) {
        super(FightEventEnum.BUFF_REMOVE_FROM_HOST);
        this.buff = buff;
        this.removeReason = removeReason;
    }

}
