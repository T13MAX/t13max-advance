package com.t13max.fight.impact;

import com.t13max.fight.action.Action_1_Attack;
import com.t13max.fight.action.IAction;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 10:43 2024/4/11
 */
public enum ImpactEnum {

    ATTACK(1, Action_1_Attack.class),
    BUFF(2, Action_1_Attack.class),

    ;

    private static Map<Integer, ImpactEnum> IMPACT_MAP = new HashMap<>();

    static {
        for (ImpactEnum impactEnum : values()) {
            IMPACT_MAP.put(impactEnum.getId(), impactEnum);
        }
    }

    // Impact逻辑对应的实现类
    @Getter
    private Class<? extends IAction> clazz;
    @Getter
    private int id;

    ImpactEnum(int id, Class<? extends IAction> clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    public static ImpactEnum getImpact(int id) {
        return IMPACT_MAP.get(id);
    }

}
