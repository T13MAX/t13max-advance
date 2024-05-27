package com.t13max.fight.buff.condition;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
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

    UNIT_DEAD(2) {
        @Override
        public IEventCondition createCondition(String param) {
            return new Condition_2_UnitDead(param);
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
