package com.t13max.fight.event;

import lombok.Getter;

import java.util.List;

/**
 * @author: t13max
 * @since: 17:10 2024/4/22
 */
@Getter
public class UnitDeadEvent extends AbstractEvent {

    private final List<Long> deadList;

    public UnitDeadEvent(List<Long> deadList) {
        super(FightEventEnum.UNIT_DEAD);
        this.deadList = deadList;
    }
}
