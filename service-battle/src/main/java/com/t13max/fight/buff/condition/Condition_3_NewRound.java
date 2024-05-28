package com.t13max.fight.buff.condition;

import com.t13max.fight.event.FightEventEnum;
import com.t13max.fight.event.IFightEvent;
import com.t13max.fight.event.UnitDeadEvent;


/**
 * 角色死亡
 *
 * @author: t13max
 * @since: 11:25 2024/4/11
 */
public class Condition_3_NewRound extends AbstractEventCondition {

    public Condition_3_NewRound(String param) {
        subEvent(FightEventEnum.SMALL_ROUND_BEGIN);
    }

    @Override
    public ConditionEnum getConditionEnum() {
        return ConditionEnum.NEW_ROUND;
    }

    @Override
    public boolean isMatch(IFightEvent event) {
        switch (event.getFightEventEnum()) {
            case SMALL_ROUND_BEGIN -> {
                return true;
            }
        }
        return false;
    }


}
