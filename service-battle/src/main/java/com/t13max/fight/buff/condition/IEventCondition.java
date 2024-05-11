package com.t13max.fight.buff.condition;


import com.t13max.fight.buff.effect.AbstractEffect;
import com.t13max.fight.event.IFightEvent;

/**
 * @author: t13max
 * @since: 11:20 2024/4/11
 */
public interface IEventCondition {

    boolean isMatch(AbstractEffect effect,IFightEvent event);

}
