package com.t13max.fight.buff.effect;

import com.t13max.fight.FightContext;
import com.t13max.fight.FightHero;
import com.t13max.fight.buff.RemoveReason;

/**
 * 属性改变效果
 *
 * @author: t13max
 * @since: 16:09 2024/4/10
 */
public class Buff_Effect_1_Attr extends AbstractEffect {

    @Override
    protected void onInit() {
        modify(true);
    }

    @Override
    public void onDestroy(RemoveReason reason) {
        super.onDestroy(reason);
        modify(false);
    }

    private void modify(boolean add) {
        FightContext fightContext = this.buffBox.getFightContext();
        FightHero fightHero = fightContext.getFightMatch().getFightHero(buffBox.getOwnerId());
        fightHero.getFightAttrManager().modifyAttr(this.param, add);
    }
}
