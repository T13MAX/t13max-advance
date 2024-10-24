package com.t13max.fight.context;

import com.t13max.fight.action.AbstractAction;
import com.t13max.fight.impact.IImpact;
import com.t13max.fight.trigger.ITrigger;
import com.t13max.fight.trigger.TriggerTimeline;
import lombok.Getter;

import java.util.Collection;

/**
 * 战斗"时光机"
 *
 * @author: t13max
 * @since: 11:34 2024/4/11
 */
public class FightTimeMachine {

    private final TriggerTimeline triggerTimeline;

    @Getter
    private final FightContext fightContext;

    public FightTimeMachine(FightContext fightContext) {
        this.fightContext = fightContext;
        this.triggerTimeline = new TriggerTimeline();
    }

    public void roll() {
        while (triggerTimeline.hasTriggerRemains()) {
            tick();
        }
    }

    public void addImpactToTimeLine(IImpact iImpact) {
        triggerTimeline.add(iImpact);
    }

    private void removeImpactFromTimeLine(IImpact iImpact) {
        triggerTimeline.remove(iImpact);
    }

    public void tick() {
        Collection<ITrigger> triggers = triggerTimeline.getNextTriggerList();
        for (ITrigger trigger : triggers) {

            //时间到, 执行
            trigger.onTimeIsUp();

            //如果是行动 执行完销毁
            if (trigger instanceof AbstractAction action) {
                action.onDestroy();
            }
        }
    }

}
