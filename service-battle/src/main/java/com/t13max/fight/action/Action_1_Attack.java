package com.t13max.fight.action;

import com.t13max.fight.FightHero;
import com.t13max.fight.FightImpl;
import com.t13max.fight.FightTimeMachine;
import com.t13max.fight.damage.CommonDamageCalculator;
import com.t13max.fight.event.ReadyToSubHpEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 普通攻击
 *
 * @author: t13max
 * @since: 10:41 2024/4/11
 */
public class Action_1_Attack extends AbstractAction {

    @Override
    public void handleCreate() {
        super.handleCreate();
    }

    @Override
    public void onTimeIsUp() {
        FightTimeMachine fightTimeMachine = this.getFightTimeMachine();
        FightImpl fight = fightTimeMachine.getFight();
        FightHero fightHero = fight.getFightHero(this.getGenerator(), this.isAttacker());
        List<Long> targetHeroIds = this.getTargetHeroIds();

        Map<Long, Double> damageMap = new HashMap<>();

        for (Long targetHeroId : targetHeroIds) {
            FightHero targetHero = fight.getFightHero(targetHeroId, !this.isAttacker());
            double damage = new CommonDamageCalculator(fightHero, targetHero).calcDamage();

            damageMap.put(targetHeroId, damage);
        }

        postEvent(new ReadyToSubHpEvent(fightHero.getId(), damageMap));
    }


}
