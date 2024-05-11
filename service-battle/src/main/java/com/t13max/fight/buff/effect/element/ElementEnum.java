package com.t13max.fight.buff.effect.element;

import com.t13max.fight.impact.ImpactEnum;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 10:09 2024/4/23
 */
@Getter
public enum ElementEnum {

    WATER(1),

    FIRE(2),

    RAY(3),

    //GRASS(4),

    //WIND(5),

    //ICE(6),

    //ROCK(7),
    ;

    private static Map<Integer, ElementEnum> ELEMENT_MAP = new HashMap<>();

    static {
        for (ElementEnum elementEnum : values()) {
            ELEMENT_MAP.put(elementEnum.getId(), elementEnum);
        }
    }

    private int id;

    ElementEnum(int id) {
        this.id = id;
    }

    public static ElementEnum getElement(int id) {
        return ELEMENT_MAP.get(id);
    }
}
