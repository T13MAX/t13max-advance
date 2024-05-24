package com.t13max.fight.damage;

import com.t13max.fight.FightHero;
import com.t13max.fight.attr.FightAttrEnum;
import com.t13max.fight.attr.FightAttrManager;

/**
 * @author: t13max
 * @since: 18:30 2024/4/15
 */
public class CommonDamageCalculator {

    private FightHero attacker;

    private FightHero defender;

    private String param;

    public CommonDamageCalculator(FightHero attacker, FightHero defender, String param) {
        this.attacker = attacker;
        this.defender = defender;
        this.param = param;
    }

    public double calcDamage() {

        FightAttrManager fightAttrManager = attacker.getFightAttrManager();

        FightAttrManager targetAttrManager = defender.getFightAttrManager();
        Double defValue = targetAttrManager.getFinalAttr(FightAttrEnum.DEF);

        String[] split = param.split(",");
        int attr = Integer.parseInt(split[0]);

        FightAttrEnum fightAttrEnum = FightAttrEnum.getFightAttrEnum(attr);
        Double atkValue = fightAttrManager.getFinalAttr(fightAttrEnum);

        int rate = Integer.parseInt(split[1]);
        double finalDamage = Math.max(1, atkValue * (rate / CalcConst.MAX_RATE) - defValue);
        //一系列复杂的计算

        return finalDamage;
    }
}
