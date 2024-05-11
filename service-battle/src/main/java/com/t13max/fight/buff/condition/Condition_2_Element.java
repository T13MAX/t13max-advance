package com.t13max.fight.buff.condition;

import com.t13max.fight.buff.effect.AbstractEffect;
import com.t13max.fight.buff.effect.Buff_Effect_2_Element;
import com.t13max.fight.event.IFightEvent;

/**
 * 元素自然消亡
 *
 * @author: t13max
 * @since: 11:25 2024/4/11
 */
public class Condition_2_Element implements IEventCondition {

    @Override
    public boolean isMatch(AbstractEffect effect,IFightEvent event) {
        if (effect instanceof Buff_Effect_2_Element buffEffect2Element) {
            return buffEffect2Element.getAmount() <= 0;
        }
        return false;
    }


}
