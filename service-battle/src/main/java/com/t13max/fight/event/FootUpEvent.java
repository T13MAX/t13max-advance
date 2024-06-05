package com.t13max.fight.event;

import com.t13max.fight.context.FightMatch;
import lombok.Getter;

/**
 * @author: t13max
 * @since: 18:40 2024/4/22
 */
@Getter
public class FootUpEvent extends AbstractEvent {

    private FightMatch fight;

    public FootUpEvent(FightMatch fight) {
        super(FightEventEnum.FOOT_UP);
        this.fight = fight;
    }
}
