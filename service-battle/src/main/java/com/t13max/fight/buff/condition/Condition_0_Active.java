package com.t13max.fight.buff.condition;

import com.t13max.fight.buff.effect.AbstractEffect;
import com.t13max.fight.event.IFightEvent;

/**
 * 直接生效
 *
 * @author: t13max
 * @since: 14:56 2024/4/23
 */
public class Condition_0_Active implements IEventCondition {
    @Override
    public boolean isMatch(AbstractEffect effect, IFightEvent event) {
        return true;
    }
}
