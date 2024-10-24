package com.t13max.fight.buff.effect.element;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 10:09 2024/4/23
 */
@Getter
public enum ElementEnum {

    ANY(0),

    WATER(1),//水 生命

    FIRE(2),//火 攻击

    RAY(3),//雷 充能(减冷却)

    GRASS(4),//草

    WIND(5),//风 (扩散)

    LIGHT(6),//光

    DARK(7),//暗


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
