package com.t13max.fight.buff.effect;

import battle.event.entity.RemoveReason;
import com.t13max.fight.attr.FightAttrEnum;
import com.t13max.fight.context.FightContext;
import com.t13max.fight.buff.condition.ConditionEnum;
import com.t13max.fight.damage.CalcConst;
import com.t13max.fight.event.FightEventBus;
import com.t13max.fight.event.IFightEvent;
import com.t13max.fight.event.ReadyToSubHpEvent;
import com.t13max.fight.hero.FightHero;
import com.t13max.game.Const;

import java.util.Collections;

/**
 * 泯灭
 *
 * @author: t13max
 * @since: 16:09 2024/4/10
 */
public class Buff_Effect_4_Vanish extends AbstractEffect {

    private int overlay = 10;

    private int rate = 1;

    @Override
    protected void onInit() {
        String[] split = this.param.split(",");
        if (split.length > 0) {
            overlay = Integer.parseInt(split[0]);
        }
        if (split.length > 1) {
            rate = Integer.parseInt(split[1]);
        }
        //角色死亡 需要自己订阅
        String param = String.valueOf(this.buffBox.getOwnerId());
        addDisposedCondition(ConditionEnum.UNIT_DEAD.createCondition(param));
    }

    @Override
    public void onDestroy(RemoveReason reason) {

    }

    @Override
    protected void doOnEvent(IFightEvent event) {
        switch (event.getFightEventEnum()) {
            case SMALL_ROUND_END -> {
                FightContext fightContext = this.buffBox.getFightContext();
                FightEventBus fightEventBus = fightContext.getFightEventBus();
                FightHero fightHero = fightContext.getFightMatch().getFightHero(this.buffBox.getOwnerId());
                if (fightHero == null) {
                    return;
                }
                Double curHp = fightHero.getFightAttrManager().getFinalAttr(FightAttrEnum.CUR_HP);
                double damage = curHp * overlay * rate / CalcConst.MAX_RATE;
                fightEventBus.postEvent(new ReadyToSubHpEvent(0, Collections.singletonMap(buffBox.getOwnerId(), damage)));
            }
        }
    }
}
