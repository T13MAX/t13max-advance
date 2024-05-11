package com.t13max.fight.event;

import lombok.Data;
import lombok.Getter;

/**
 * @author: t13max
 * @since: 15:02 2024/4/15
 */
@Getter
public class SmallRoundBeginEvent extends AbstractEvent {

    private long heroId;

    private int round;

    public SmallRoundBeginEvent(long heroId, int round) {
        super(FightEventEnum.SMALL_ROUND_BEGIN);
        this.heroId = heroId;
        this.round = round;
    }
}
