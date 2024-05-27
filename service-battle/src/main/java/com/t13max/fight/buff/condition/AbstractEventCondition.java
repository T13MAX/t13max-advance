package com.t13max.fight.buff.condition;

import com.t13max.fight.event.FightEventEnum;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: t13max
 * @since: 14:17 2024/5/27
 */
public abstract class AbstractEventCondition implements IEventCondition {

    private List<FightEventEnum> fightEventEnums;

    @Override
    public final List<FightEventEnum> getFightEventEnum() {
        if (fightEventEnums == null) {
            return Collections.emptyList();
        }
        return fightEventEnums;
    }

    protected final void subEvent(FightEventEnum... eventEnums) {
        if (fightEventEnums == null) {
            fightEventEnums = new LinkedList<>();
        }
        Collections.addAll(fightEventEnums, eventEnums);
    }

}
