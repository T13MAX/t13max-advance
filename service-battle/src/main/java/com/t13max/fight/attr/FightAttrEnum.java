package com.t13max.fight.attr;

/**
 * @author: t13max
 * @since: 15:02 2024/4/10
 */
public enum FightAttrEnum {

    ATTACK {
        @Override
        public double modifyValue(Double oldValue, double modifyValue, boolean add) {
            if (add) {
                return oldValue + modifyValue;
            } else {
                //最低1攻击力
                return Math.max(1, oldValue - modifyValue);
            }
        }
    },

    DEF,

    CUR_HP,

    MAX_HP,

    SPEED,
    ELEMENT,
    MASTERY,

    ;

    public double modifyRate(Double oldValue, double modifyValue, boolean add) {

        if (oldValue == null) {
            oldValue = 0D;
        }

        if (add) {
            return oldValue + modifyValue;
        } else {
            return oldValue - modifyValue;
        }
    }

    public double modifyValue(Double oldValue, double modifyValue, boolean add) {

        if (oldValue == 0) {
            oldValue = 0D;
        }

        if (add) {
            return oldValue + modifyValue;
        } else {
            return Math.max(0, oldValue - modifyValue);
        }
    }
}
