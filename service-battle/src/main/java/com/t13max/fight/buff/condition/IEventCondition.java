package com.t13max.fight.buff.condition;

import com.t13max.fight.event.FightEventEnum;
import com.t13max.fight.event.IFightEvent;

import java.util.List;

/**
 * 事件条件
 *
 * @author: t13max
 * @since: 11:20 2024/4/11
 */
public interface IEventCondition extends ICondition{

    ConditionEnum getConditionEnum();

    List<FightEventEnum> getFightEventEnum();

    boolean isMatch(IFightEvent event);

}
