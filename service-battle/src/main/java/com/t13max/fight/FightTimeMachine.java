package com.t13max.fight;

import com.t13max.fight.action.IAction;
import com.t13max.fight.impact.IImpact;
import com.t13max.fight.trigger.ITrigger;
import com.t13max.fight.trigger.TriggerTimeline;
import lombok.Getter;

import java.util.Collection;

/**
 * @author: t13max
 * @since: 11:34 2024/4/11
 */
public class FightTimeMachine {

    private TriggerTimeline triggerTimeline;

    @Getter
    private FightImpl fight;

    public FightTimeMachine(FightImpl fight) {
        this.fight = fight;
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
            trigger.onTimeIsUp();
        }
    }

}
