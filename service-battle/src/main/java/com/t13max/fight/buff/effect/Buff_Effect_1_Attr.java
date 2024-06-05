package com.t13max.fight.buff.effect;

import battle.event.entity.RemoveReason;
import com.t13max.fight.context.FightContext;
import com.t13max.fight.hero.FightHero;
import com.t13max.fight.event.IFightEvent;

/**
 * 属性改变效果
 *
 * @author: t13max
 * @since: 16:09 2024/4/10
 */
public class Buff_Effect_1_Attr extends AbstractEffect {

    @Override
    protected void onInit() {

    }

    @Override
    public void onDestroy(RemoveReason reason) {
        super.onDestroy(reason);
        modify(false);
    }

    @Override
    protected void handleActive() {
        modify(true);
    }

    @Override
    protected void doOnEvent(IFightEvent event) {

    }

    private void modify(boolean add) {
        FightContext fightContext = this.buffBox.getFightContext();
        FightHero fightHero = fightContext.getFightMatch().getFightHero(buffBox.getOwnerId());
        fightHero.getFightAttrManager().modifyAttr(this.param, add);
        fightHero.getFightAttrManager().calcFinalAttr();
    }
}
