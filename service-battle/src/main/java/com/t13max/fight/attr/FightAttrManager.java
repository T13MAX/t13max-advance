package com.t13max.fight.attr;

import com.t13max.fight.hero.FightHero;
import com.t13max.game.exception.BattleException;
import com.t13max.game.util.Log;
import com.t13max.template.helper.HeroHelper;
import com.t13max.template.manager.TemplateManager;
import com.t13max.template.temp.TemplateHero;
import com.t13max.util.StringUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author: t13max
 * @since: 15:06 2024/4/10
 */
public class FightAttrManager {

    private final Map<FightAttrEnum, Double> valueAttrMap = new HashMap<>();

    private final Map<FightAttrEnum, Double> rateAttrMap = new HashMap<>();

    private final Map<FightAttrEnum, Double> finalAttrMap = new HashMap<>();

    @Getter
    private boolean die;

    private final FightHero owner;

    public FightAttrManager(FightHero fightHero) {
        this.owner = fightHero;
        initAttr();
    }

    private void initAttr() {
        HeroHelper heroHelper = TemplateManager.inst().helper(HeroHelper.class);
        TemplateHero template = heroHelper.getTemplate(owner.getTemplateId());
        if (template == null) {
            throw new BattleException("TemplateHero为空, id=" + owner.getTemplateId());
        }
        valueAttrMap.put(FightAttrEnum.ATTACK, (double) template.atk);
        valueAttrMap.put(FightAttrEnum.MAX_HP, (double) template.hp);
        valueAttrMap.put(FightAttrEnum.DEF, (double) template.def);
        valueAttrMap.put(FightAttrEnum.SPEED, (double) template.speed);
        valueAttrMap.put(FightAttrEnum.ELEMENT, (double) template.element);
        valueAttrMap.put(FightAttrEnum.MASTERY, (double) template.mastery);

        //临时
        Random random = new Random();
        rateAttrMap.put(FightAttrEnum.ATTACK, random.nextDouble(100));
        rateAttrMap.put(FightAttrEnum.DEF, random.nextDouble(100));
        rateAttrMap.put(FightAttrEnum.MAX_HP, random.nextDouble(100));

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
        double newValue = attrEnum.modifyRate(this, oldValue, value, add);
        //if (attrEnum == FightAttrEnum.MAX_HP) reCalcHp();
        this.rateAttrMap.put(attrEnum, newValue);
    }

    public void modifyValueAttr(FightAttrEnum attrEnum, double value, boolean add) {
        if (attrEnum == FightAttrEnum.CUR_HP) {
            Log.battle.error("禁止使用modifyValueAttr修改当前血量");
            return;
        }
        Double oldValue = this.valueAttrMap.get(attrEnum);
        double newValue = attrEnum.modifyValue(this, oldValue, value, add);
        //if (attrEnum == FightAttrEnum.MAX_HP) reCalcHp();
        this.valueAttrMap.put(attrEnum, newValue);
    }

    public void modifyAttr(String param, boolean add) {
        String[] split = param.split(StringUtil.ASTERISK);
        for (String string : split) {
            String[] split1 = string.split(StringUtil.COLON);
            int attr = Integer.parseInt(split1[0]);
            int rate = Integer.parseInt(split1[1]);
            int value = Integer.parseInt(split1[2]);
            FightAttrEnum fightAttrEnum = FightAttrEnum.getFightAttrEnum(attr);
            if (rate == 0) {
                modifyValueAttr(fightAttrEnum, value, add);
            } else if (rate == 1) {
                modifyRateAttr(fightAttrEnum, value, add);
            }
        }
    }

    public Map<Integer, Double> getAttr() {
        Map<Integer, Double> attr = new HashMap<>();
        for (Map.Entry<FightAttrEnum, Double> entry : this.finalAttrMap.entrySet()) {
            attr.put(entry.getKey().getId(), entry.getValue());
        }
        return attr;
    }

    /**
     * //如果调整了最大生命值 则当前生命值同步修改
     *
     * @Author t13max
     * @Date 19:42 2024/5/27
     */
    private void reCalcHp() {
        Double oldValue = 0D;
        double newValue = 0D;

        Double oldCurHp = getFinalAttr(FightAttrEnum.CUR_HP);
        double rate = oldCurHp / oldValue;
        setCurHp(newValue * rate);
    }
}

