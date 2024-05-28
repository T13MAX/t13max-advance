package com.t13max.fight.action;


import com.t13max.fight.event.IFightEvent;
import com.t13max.fight.impact.AbstractImpact;

/**
 * 抽象行动类
 *
 * @author: t13max
 * @since: 10:48 2024/4/11
 */
public abstract class AbstractAction extends AbstractImpact implements IAction {

    @Override
    public final void onEvent(IFightEvent event) {

    }

    @Override
    public void onCreate() {
        //订阅一些事件之类的
        fightContext.getFightEventBus().register(this);
        handleCreate();
    }

    @Override
    public void onDestroy() {
        fightContext.getFightEventBus().unregister(this);
    }

    @Override
    public boolean paramCheck() {
        return true;
    }

    public void handleCreate() {
    }

    public void postEvent(IFightEvent event) {
        this.fightContext.getFightEventBus().postEvent(event);
    }
}
