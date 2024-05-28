package com.t13max.fight.damage;

import com.t13max.fight.hero.FightHero;
import com.t13max.fight.attr.FightAttrEnum;
import com.t13max.fight.attr.FightAttrManager;
import com.t13max.fight.buff.effect.element.ElementEnum;

/**
 * @author: t13max
 * @since: 18:30 2024/4/15
 */
@Deprecated
public class ElementDamageCalculator {

    private FightHero attacker;

    private FightHero defender;

    private ElementEnum elementEnum;

    private double amount;

    private ElementEnum targetElementEnum;

    private double targetAmount;

    public ElementDamageCalculator(ElementEnum elementEnum, double amount, ElementEnum targetElementEnum, double targetAmount, FightHero attacker, FightHero defender) {
        this.attacker = attacker;
        this.defender = defender;
        this.elementEnum = elementEnum;
        this.targetElementEnum = targetElementEnum;
        this.targetAmount = targetAmount;
        this.amount = amount;
    }

    public double calcDamage() {

        FightAttrManager fightAttrManager = attacker.getFightAttrManager();
        Double atkValue = fightAttrManager.getFinalAttr(FightAttrEnum.ATTACK);

        FightAttrManager targetAttrManager = defender.getFightAttrManager();
        Double defValue = targetAttrManager.getFinalAttr(FightAttrEnum.DEF);

        double min = Math.min(amount, targetAmount);

        Double mastery = this.attacker.getFightAttrManager().getFinalAttr(FightAttrEnum.MASTERY);
        double finalDamage = Math.max(1, atkValue * (1 + mastery * min / 1000) * 1 - defValue);
        //一系列复杂的计算

        return finalDamage;
    }
}
