package com.t13max.fight.buff.effect.element;

import lombok.Getter;

/**
 * @author: t13max
 * @since: 15:38 2024/4/23
 */
@Getter
public enum ReactEnum {

    //火水蒸发
    EVAPORATION(ElementEnum.FIRE, ElementEnum.WATER),

    //火雷超载
    OVERLOADING(ElementEnum.FIRE, ElementEnum.RAY),

    //水雷感电
    ELECTRIFY(ElementEnum.WATER, ElementEnum.RAY),
    ;

    private ElementEnum elementEnum1;
    private ElementEnum elementEnum2;

    ReactEnum(ElementEnum elementEnum1, ElementEnum elementEnum2) {
        this.elementEnum1 = elementEnum1;
        this.elementEnum2 = elementEnum2;
    }

    public static ReactEnum getReactEnum(ElementEnum elementEnum1, ElementEnum elementEnum2) {
        for (ReactEnum reactEnum : values()) {
            if (reactEnum.getElementEnum1() == elementEnum1) {
                if (reactEnum.getElementEnum2() == elementEnum2) {
                    return reactEnum;
                }
            } else if (reactEnum.getElementEnum1() == elementEnum2) {
                if (reactEnum.getElementEnum2() == elementEnum1) {
                    return reactEnum;
                }
            }
        }
        return null;
    }
}
