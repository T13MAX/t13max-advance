package com.t13max.fight.attr;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 15:02 2024/4/10
 */
@Getter
public enum FightAttrEnum {

    ATTACK(10) {
        @Override
        public double modifyValue(FightAttrManager fightAttrManager, Double oldValue, double modifyValue, boolean add) {
            if (add) {
                return oldValue + modifyValue;
            } else {
                //最低1攻击力
                return Math.max(1, oldValue - modifyValue);
            }
        }
    },
    //防御力
    DEF(20),
    //当前生命值
    CUR_HP(30) {
        @Override
        public double modifyRate(FightAttrManager fightAttrManager, Double oldValue, double modifyValue, boolean add) {
            //当前生命值没有比例
            return oldValue;
        }

        @Override
        public double modifyValue(FightAttrManager fightAttrManager, Double oldValue, double modifyValue, boolean add) {
            double newValue = super.modifyValue(fightAttrManager, oldValue, modifyValue, add);
            Double maxValue = fightAttrManager.getFinalAttr(FightAttrEnum.MAX_HP);
            //不能超过最大生命值
            return Math.min(newValue, maxValue);
        }
    },
    //最大生命值
    MAX_HP(40),
    //速度
    SPEED(50),
    //元素
    ELEMENT(60),
    //属性精通
    MASTERY(70),
    //受到伤害增加
    VULNERABILITY(80),
    //造成伤害增加
    DAMAGE_INCREASE(90),
    //充能 满了释放发招
    CHARGE(100) {

    },
    //生命恢复
    HP_RECOVER(110),
    ;

    private static Map<Integer, FightAttrEnum> fightAttrEnumMap = new HashMap<>();

    static {
        for (FightAttrEnum fightAttrEnum : values()) {
            fightAttrEnumMap.put(fightAttrEnum.getId(), fightAttrEnum);
        }
    }

    private final int id;

    FightAttrEnum(int id) {
        this.id = id;
    }

    public double modifyRate(FightAttrManager fightAttrManager, Double oldValue, double modifyValue, boolean add) {

        if (oldValue == null) {
            oldValue = 0D;
        }

        if (add) {
            return oldValue + modifyValue;
        } else {
            //默认百分比允许负数 就是减益效果
            return oldValue - modifyValue;
        }
    }

    public double modifyValue(FightAttrManager fightAttrManager, Double oldValue, double modifyValue, boolean add) {

        if (oldValue == null) {
            oldValue = 0D;
        }

        if (add) {
            return oldValue + modifyValue;
        } else {
            //默认基础数值最低就是0
            return Math.max(0, oldValue - modifyValue);
        }
    }

    public static FightAttrEnum getFightAttrEnum(int id) {
        return fightAttrEnumMap.get(id);
    }
}
