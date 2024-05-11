package com.t13max.fight.damage;

import com.t13max.fight.FightHero;
import com.t13max.fight.attr.FightAttrEnum;
import com.t13max.fight.attr.FightAttrManager;
import com.t13max.template.TemplateSkill;

/**
 * @author: t13max
 * @since: 18:30 2024/4/15
 */
public class CommonDamageCalculator {

    private FightHero attacker;

    private FightHero defender;

    public CommonDamageCalculator(FightHero attacker, FightHero defender) {
        this.attacker = attacker;
        this.defender = defender;
    }

    public double calcDamage() {

        FightAttrManager fightAttrManager = attacker.getFightAttrManager();
        Double atkValue = fightAttrManager.getFinalAttr(FightAttrEnum.ATTACK);

        FightAttrManager targetAttrManager = defender.getFightAttrManager();
        Double defValue = targetAttrManager.getFinalAttr(FightAttrEnum.DEF);

        double finalDamage = Math.max(1, atkValue - defValue);
        //一系列复杂的计算

        return finalDamage;
    }
}
