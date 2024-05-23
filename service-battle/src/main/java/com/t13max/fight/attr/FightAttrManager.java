package com.t13max.fight.attr;

import com.t13max.fight.FightHero;
import com.t13max.game.exception.BattleException;
import com.t13max.template.helper.HeroHelper;
import com.t13max.template.manager.TemplateManager;
import com.t13max.template.temp.TemplateHero;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author: t13max
 * @since: 15:06 2024/4/10
 */
public class FightAttrManager {

    private Map<FightAttrEnum, Double> valueAttrMap = new HashMap<>();

    private Map<FightAttrEnum, Double> rateAttrMap = new HashMap<>();

    private Map<FightAttrEnum, Double> finalAttrMap = new HashMap<>();

    @Getter
    private boolean die;

    private FightHero owner;

    public FightAttrManager(FightHero fightHero) {
        this.owner = fightHero;
        initAttr();
    }

    private void initAttr() {
        Random random = new Random();
        HeroHelper heroHelper = TemplateManager.inst().helper(HeroHelper.class);
        TemplateHero template = heroHelper.getTemplate(owner.getTemplateId());
        if (template == null) {
            throw new BattleException("TemplateHero为空, id=" + owner.getTemplateId());
        }
        valueAttrMap.put(FightAttrEnum.ATTACK, (double) template.getAtk());
        valueAttrMap.put(FightAttrEnum.MAX_HP, (double) template.getHp());
        valueAttrMap.put(FightAttrEnum.SPEED, (double) template.getSpeed());
        valueAttrMap.put(FightAttrEnum.ELEMENT, (double) template.getElement());
        valueAttrMap.put(FightAttrEnum.MASTERY, (double) template.getMastery());

        //临时
        for (FightAttrEnum attrEnum : FightAttrEnum.values()) {
            rateAttrMap.put(attrEnum, random.nextDouble(10));
        }
        rateAttrMap.put(FightAttrEnum.ATTACK, random.nextDouble(100));

        calcFinalAttr();

        //设置一下当前生命
        this.setCurHp(this.getFinalAttr(FightAttrEnum.MAX_HP));
    }

    public void calcFinalAttr() {
        for (FightAttrEnum attrEnum : FightAttrEnum.values()) {
            Double value = valueAttrMap.get(attrEnum);
            Double rate = rateAttrMap.get(attrEnum);
            if (value == null) {
                continue;
            }
            if (rate == null) {
                rate = 0D;
            }
            finalAttrMap.put(attrEnum, value * (1 + rate / 100));
        }

    }

    public Double getFinalAttr(FightAttrEnum fightAttrEnum) {
        Double value = this.finalAttrMap.get(fightAttrEnum);
        if (value == null) {
            return 0D;
        }
        return value;
    }

    public Double getFinalAttr(FightAttrEnum fightAttrEnum, boolean reCalc) {
        if (reCalc) calcFinalAttr();
        return getFinalAttr(fightAttrEnum);
    }

    public void subHp(double value) {
        this.modifyHp(value, false);
    }

    public void addHp(double value) {
        this.modifyHp(value, true);
    }

    private void modifyHp(double value, boolean add) {
        if (add) {
            this.setCurHp(this.getFinalAttr(FightAttrEnum.CUR_HP) + value);
        } else {
            this.setCurHp(this.getFinalAttr(FightAttrEnum.CUR_HP) - value);
        }
    }

    public void setCurHp(double value) {
        this.finalAttrMap.put(FightAttrEnum.CUR_HP, value);
    }

    public boolean isAlive() {
        return !die && this.getFinalAttr(FightAttrEnum.CUR_HP) > 0;
    }

    public boolean isVerge2Death() {
        return !die && this.getFinalAttr(FightAttrEnum.CUR_HP) <= 0;
    }

    public void modifyRateAttr(FightAttrEnum attrEnum, double value, boolean add) {
        Double oldValue = this.rateAttrMap.get(attrEnum);
        double newValue = attrEnum.modifyRate(oldValue, value, add);
        this.rateAttrMap.put(attrEnum, newValue);
    }

    public void modifyValueAttr(FightAttrEnum attrEnum, double value, boolean add) {
        Double oldValue = this.valueAttrMap.get(attrEnum);
        double newValue = attrEnum.modifyValue(oldValue, value, add);
        this.valueAttrMap.put(attrEnum, newValue);
    }

}

