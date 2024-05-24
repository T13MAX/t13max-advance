package com.t13max.fight.buff.effect.element;

import lombok.Getter;

/**
 * @author: t13max
 * @since: 15:38 2024/4/23
 */
@Getter
public enum ReactEnum {

    //火水蒸发 伤害
    EVAPORATION(ElementEnum.FIRE, ElementEnum.WATER),

    //火雷超载 击飞 大硬直
    OVERLOADING(ElementEnum.FIRE, ElementEnum.RAY),

    //水雷感电 麻痹 持续控制
    ELECTRIFY(ElementEnum.WATER, ElementEnum.RAY),

    //火草燃烧 持续伤害
    BURNING(ElementEnum.FIRE, ElementEnum.GRASS),

    //水草产种 召唤
    SEED(ElementEnum.WATER, ElementEnum.GRASS),

    //雷草绽放 护盾
    BLOOM(ElementEnum.RAY, ElementEnum.GRASS),

    //风扩散
    DIFFUSE(ElementEnum.WIND, ElementEnum.ANY),

    //光 机制 真实伤害 无视防御 净化

    //暗 机制 复活 凭空召唤

    ;

    private final ElementEnum elementEnum1;
    private final ElementEnum elementEnum2;

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
