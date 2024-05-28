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
public class Condition_10_UnitDead extends AbstractEventCondition {

    private long targetId;

    public Condition_10_UnitDead(String param) {
        this.targetId = Long.parseLong(param);
        subEvent(FightEventEnum.UNIT_DEAD);
    }

    @Override
    public ConditionEnum getConditionEnum() {
        return ConditionEnum.UNIT_DEAD;
    }

    @Override
    public boolean isMatch(IFightEvent event) {
        switch (event.getFightEventEnum()) {
            case UNIT_DEAD -> {
                UnitDeadEvent unitDeadEvent = (UnitDeadEvent) event;
                if (unitDeadEvent.getDeadList().contains(targetId)) {
                    return true;
                }
            }
        }
        return false;
    }


}
