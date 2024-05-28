package com.t13max.fight.action;

import com.t13max.fight.FightHero;
import com.t13max.fight.FightMatch;
import com.t13max.fight.FightTimeMachine;
import com.t13max.fight.damage.DefaultDamageCalculator;
import com.t13max.fight.event.ReadyToSubHpEvent;
import com.t13max.util.Log;

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
    public boolean paramCheck() {
        String[] split = this.param.split(",");
        if (split.length < 2) {
            Log.battle.error("Action_1_Attack.paramCheck, split.length < 2, id={}", this.skillId);
            return false;
        }
        return true;
    }

    @Override
    public void onTimeIsUp() {
        FightMatch fightMatch = fightContext.getFightMatch();
        FightHero fightHero = fightMatch.getFightHero(this.getGenerator());
        List<Long> targetHeroIds = this.getTargetHeroIds();
        Map<Long, Double> damageMap = new HashMap<>();

        for (Long targetHeroId : targetHeroIds) {
            FightHero targetHero = fightMatch.getFightHero(targetHeroId);
            double damage = new DefaultDamageCalculator(fightHero, targetHero, this.param).calcDamage();
            damageMap.put(targetHeroId, damage);
        }

        postEvent(new ReadyToSubHpEvent(fightHero.getId(), damageMap));
    }


}
