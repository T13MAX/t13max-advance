package com.t13max.fight.impact;

import com.t13max.fight.attr.FightAttrEnum;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 14:59 2024/5/24
 */
public enum SelectorEnum {

    SELF(1),

    TARGET(2),//包含多个

    SELF_RANDOM(3),

    OTHER_RANDOM(4),

    ;
    private static Map<Integer, SelectorEnum> selectorEnumMap = new HashMap<>();

    static {
        for (SelectorEnum selectorEnum : values()) {
            selectorEnumMap.put(selectorEnum.getId(), selectorEnum);
        }
    }

    @Getter
    private int id;

    SelectorEnum(int id) {
        this.id = id;
    }

    public static SelectorEnum getSelectorEnum(int id) {
        return selectorEnumMap.get(id);
    }
}
