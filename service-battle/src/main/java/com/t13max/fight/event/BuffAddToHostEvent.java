package com.t13max.fight.event;

import com.t13max.fight.buff.IBuffBox;
import lombok.Getter;

/**
 * buff添加到玩家身上
 *
 * @Author t13max
 * @Date 11:23 2024/5/28
 */
@Getter
public class BuffAddToHostEvent extends AbstractEvent {

    private final IBuffBox buff;

    public BuffAddToHostEvent(IBuffBox buff) {
        super(FightEventEnum.BUFF_ADD_TO_HOST);
        this.buff = buff;
    }

}
