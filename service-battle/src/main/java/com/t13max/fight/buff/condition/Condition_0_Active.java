package com.t13max.fight.buff.condition;

import com.t13max.fight.event.FightEventEnum;
import com.t13max.fight.event.IFightEvent;

/**
 * 直接生效
 *
 * @author: t13max
 * @since: 14:56 2024/4/23
 */
public class Condition_0_Active extends AbstractEventCondition {


    public Condition_0_Active() {
        subEvent(FightEventEnum.BUFF_ADD_TO_HOST);
    }

    @Override
    public ConditionEnum getConditionEnum() {
        return ConditionEnum.AT_ONCE;
    }

    @Override
    public boolean isMatch(IFightEvent event) {
        return true;
    }
}
