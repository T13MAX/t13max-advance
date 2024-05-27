package com.t13max.fight.buff.effect;

import com.t13max.fight.buff.BuffBoxImpl;
import com.t13max.fight.buff.BuffStatus;
import com.t13max.fight.buff.RemoveReason;
import com.t13max.fight.buff.condition.IEventCondition;
import com.t13max.fight.event.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: t13max
 * @since: 11:19 2024/4/11
 */
@Data
public abstract class AbstractEffect extends AbstractEventListener implements IBuffEffect {

    protected BuffBoxImpl buffBox;

    protected BuffStatus buffStatus = BuffStatus.IDLE;

    protected Set<BuffAffectType> buffAffectTypes = new HashSet<>();

    protected Set<IEventCondition> activeConditions = new HashSet<>();

    protected Set<IEventCondition> disposedConditions = new HashSet<>();

    protected String param;

    private int life;

    private boolean checkAnyConditionMatched(Set<IEventCondition> conditions, IFightEvent event) {
        return conditions.stream().anyMatch(eventCondition -> eventCondition.isMatch(event));
    }

    @Override
    public int reduceLife(int reduce) {
        return life -= reduce;
    }

    @Override
    public int increaseLife(int increase) {
        return life += increase;
    }

    @Override
    public final void onCreate() {
        buffStatus = BuffStatus.IDLE;

        onInit();
    }

    protected abstract void onInit();

    @Override
    public void onDestroy(RemoveReason reason) {
        //得清掉 或者用弱引用
        this.activeConditions.clear();
        this.disposedConditions.clear();
    }

    @Override
    public Set<FightEventEnum> getInterestedEvent() {
        return null;
    }

    @Override
    public void onEvent(IFightEvent event) {

        if (buffStatus == BuffStatus.IDLE) {
            // 处理事件,判断是否满足生效激活条件
            boolean canActive = checkAnyConditionMatched(activeConditions, event);

            if (canActive) {
                buffStatus = BuffStatus.ACTIVE;
                buffBox.getFightContext().getFightEventBus().postEvent(new BuffEffectCanActiveEvent(this));
                handleActive();
            }
        }

        doOnEvent(event);

        if (buffStatus == BuffStatus.ACTIVE) {
            // 处理事件,判断是否满足生效激活条件
            boolean canDisposed = checkAnyConditionMatched(disposedConditions, event);

            if (canDisposed) {
                disposed(RemoveReason.DISPOSED);
            }
        }

    }

    protected void doOnEvent(IFightEvent event) {

    }

    protected void handleActive() {

    }

    protected void onAddon(IBuffEffect addonEffect){

    }

    protected final void disposed(RemoveReason removeReason) {
        buffStatus = BuffStatus.DISPOSED;
        buffBox.getFightContext().getFightEventBus().postEvent(new BuffEffectCanDisposedEvent(this, removeReason));
    }

    protected final void addDisposedCondition(IEventCondition... eventConditions) {
        if (eventConditions != null) {
            for (IEventCondition eventCondition : eventConditions) {
                this.disposedConditions.add(eventCondition);

                subscribeEvent(eventCondition.getFightEventEnum());
            }
        }
    }

    protected final void addActiveCondition(IEventCondition... eventConditions) {
        if (eventConditions != null) {
            for (IEventCondition eventCondition : eventConditions) {
                this.activeConditions.add(eventCondition);

                subscribeEvent(eventCondition.getFightEventEnum());
            }
        }
    }
}
