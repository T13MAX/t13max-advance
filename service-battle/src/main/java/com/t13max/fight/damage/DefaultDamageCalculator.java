package com.t13max.fight.damage;

import com.t13max.fight.FightHero;
import com.t13max.fight.attr.FightAttrEnum;
import com.t13max.fight.attr.FightAttrManager;

/**
 * 默认伤害计算器
 * 攻击伤害(指定属性x倍率) x 造成伤害增幅 x 受伤增幅 x 防御力减伤(后续改为反比例函数)
 *
 * @author: t13max
 * @since: 18:30 2024/4/15
 */
public class DefaultDamageCalculator implements ICalculator {

    private FightHero attacker;

    private FightHero defender;

    private String param;

    public DefaultDamageCalculator(FightHero attacker, FightHero defender, String param) {
        this.attacker = attacker;
        this.defender = defender;
        this.param = param;
    }

    @Override
    public double calcDamage() {

        FightAttrManager fightAttrManager = attacker.getFightAttrManager();
        Double damageIncrease = fightAttrManager.getFinalAttr(FightAttrEnum.DAMAGE_INCREASE);

        FightAttrManager targetAttrManager = defender.getFightAttrManager();
        Double defValue = targetAttrManager.getFinalAttr(FightAttrEnum.DEF);
        Double vulnerability = targetAttrManager.getFinalAttr(FightAttrEnum.VULNERABILITY);

        String[] split = param.split(",");
        int attr = Integer.parseInt(split[0]);

        FightAttrEnum fightAttrEnum = FightAttrEnum.getFightAttrEnum(attr);
        Double atkValue = fightAttrManager.getFinalAttr(fightAttrEnum);


        int rate = Integer.parseInt(split[1]);
        double finalDamage = Math.max(1, atkValue * (rate / CalcConst.MAX_RATE) * (1 + damageIncrease / CalcConst.MAX_RATE) * (1 + vulnerability / CalcConst.MAX_RATE) * defValue / CalcConst.MAX_RATE);
        //一系列复杂的计算

        return finalDamage;
    }
}
