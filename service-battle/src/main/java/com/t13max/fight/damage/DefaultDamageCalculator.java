package com.t13max.fight.damage;

import com.t13max.fight.hero.FightHero;
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

        double rate = Double.parseDouble(split[1]);

        //一系列复杂的计算
        double finalDamage = atkValue * rate / CalcConst.MAX_RATE * (1 + calcDamage(damageIncrease)) * (1 + calcVul(vulnerability)) * (calcDef(defValue));

        return Math.max(1, finalDamage);
    }

    private double calcDamage(double damageIncrease) {
        double finalValue = 0;
        finalValue = damageIncrease / CalcConst.MAX_RATE;
        return finalValue;
    }

    /**
     * 计算受伤增幅
     *
     * @Author t13max
     * @Date 14:53 2024/5/28
     */
    private double calcVul(Double vulnerability) {
        double finalValue = 0;
        finalValue = vulnerability / CalcConst.MAX_RATE;
        return finalValue;
    }

    /**
     * 防御力减伤
     * y=(1/100)x
     * y=36/x
     *
     * @Author t13max
     * @Date 14:52 2024/5/28
     */
    private double calcDef(double defValue) {
        double finalValue = 1 - defValue / CalcConst.MAX_RATE;
        if (defValue >= CalcConst.DEF_CHANGE_POINT) {
            finalValue = CalcConst.DEF_INVERSE / defValue;
        }
        return finalValue;
    }
}
