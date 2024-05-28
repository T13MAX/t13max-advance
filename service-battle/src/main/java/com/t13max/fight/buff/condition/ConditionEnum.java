package com.t13max.fight.buff.condition;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 条件枚举
 * 小于10的是基础条件 走通用逻辑添加
 * 大于的特殊处理
 *
 * @author: t13max
 * @since: 13:54 2024/5/27
 */
@Getter
public enum ConditionEnum {

    //立即生效
    AT_ONCE(0) {
        @Override
        public IEventCondition createCondition(String param) {
            return new Condition_0_Active();
        }
    },

    //持续会回合数
    ROUND_COUNT(1) {
        @Override
        public IEventCondition createCondition(String param) {
            return new Condition_1_RoundCount(param);
        }
    },


    NEW_ROUND(3) {
        @Override
        public IEventCondition createCondition(String param) {
            return null;
        }
    },

    UNIT_DEAD(10) {
        @Override
        public IEventCondition createCondition(String param) {
            return new Condition_10_UnitDead(param);
        }
    },

    ;

    private int id;

    ConditionEnum(int id) {
        this.id = id;
    }

    private static final Map<Integer, ConditionEnum> eventConditionMap = new HashMap<>();

    static {
        for (ConditionEnum conditionEnum : values()) {
            eventConditionMap.put(conditionEnum.getId(), conditionEnum);
        }
    }

    public abstract IEventCondition createCondition(String param);

    public static ConditionEnum getConditionEnum(String idStr) {
        int id = Integer.parseInt(idStr);
        return getConditionEnum(id);
    }

    public static ConditionEnum getConditionEnum(int id) {
        return eventConditionMap.get(id);
    }
}
