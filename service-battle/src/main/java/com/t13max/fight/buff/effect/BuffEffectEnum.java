package com.t13max.fight.buff.effect;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 16:10 2024/4/10
 */
@Getter
public enum BuffEffectEnum {

    ATTR(1, Buff_Effect_1_Attr.class),

    ELEMENT(2, Buff_Effect_2_Element.class),

    COUNTER_WOUND(3, Buff_Effect_3_CounterWound.class),


    ;

    private static Map<Integer, BuffEffectEnum> EFFECT_MAP = new HashMap<>();

    static {
        for (BuffEffectEnum effectEnum : values()) {
            EFFECT_MAP.put(effectEnum.getId(), effectEnum);
        }
    }

    private int id;

    private Class<? extends IBuffEffect> clazz;

    BuffEffectEnum(int id, Class<? extends IBuffEffect> clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    public static BuffEffectEnum getEffect(int id) {
        return EFFECT_MAP.get(id);
    }
}
