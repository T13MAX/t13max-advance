package com.t13max.fight.buff.condition;

import com.t13max.fight.buff.effect.AbstractEffect;
import com.t13max.fight.event.IFightEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * 后续优化 多条件 且或
 *
 * @author: t13max
 * @since: 11:24 2024/4/11
 */
public class ConditionImpl implements IEventCondition {

    private Set<IEventCondition> eventConditions = new HashSet<>();

    @Override
    public boolean isMatch(AbstractEffect effect, IFightEvent event) {
        return eventConditions.stream().allMatch(e -> e.isMatch(effect, event));
    }
}
