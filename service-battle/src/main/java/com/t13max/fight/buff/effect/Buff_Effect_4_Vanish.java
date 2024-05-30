package com.t13max.fight.buff.effect;

import battle.event.entity.RemoveReason;
import com.t13max.fight.FightContext;
import com.t13max.fight.buff.condition.ConditionEnum;

/**
 * 泯灭
 *
 * @author: t13max
 * @since: 16:09 2024/4/10
 */
public class Buff_Effect_4_Vanish extends AbstractEffect {

    @Override
    protected void onInit() {
        FightContext fightContext = this.buffBox.getFightContext();
        fightContext.getFightMatch().getFightHero(buffBox.getOwnerId()).getFightAttrManager().modifyAttr(this.param, true);

        //角色死亡 需要自己订阅
        String param = String.valueOf(this.buffBox.getOwnerId());
        addDisposedCondition(ConditionEnum.UNIT_DEAD.createCondition(param));
    }

    @Override
    public void onDestroy(RemoveReason reason) {
        super.onDestroy(reason);
        FightContext fightContext = this.buffBox.getFightContext();
        fightContext.getFightMatch().getFightHero(buffBox.getOwnerId()).getFightAttrManager().modifyAttr(this.param, false);
    }
}
