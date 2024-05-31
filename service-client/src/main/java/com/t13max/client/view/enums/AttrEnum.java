package com.t13max.client.view.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 16:28 2024/5/31
 */
@Getter
public enum AttrEnum {

    ATTACK(10, Const.ATK),
    //防御力
    DEF(20, Const.DEF),
    //当前生命值
    CUR_HP(30, Const.HP),
    //最大生命值
    MAX_HP(40),
    //速度
    SPEED(50),
    //元素
    ELEMENT(60),
    //属性精通
    MASTERY(70),
    //受到伤害增加
    VULNERABILITY(80),
    //造成伤害增加
    DAMAGE_INCREASE(90),
    //充能 满了释放发招
    CHARGE(100),

    ;
    private static Map<Integer, AttrEnum> fightAttrEnumMap = new HashMap<>();

    static {
        for (AttrEnum fightAttrEnum : values()) {
            fightAttrEnumMap.put(fightAttrEnum.getId(), fightAttrEnum);
        }
    }

    private int id;

    private String name;

    AttrEnum(int id) {
        this.id = id;
    }

    AttrEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean hasName() {
        return this.name != null;
    }

    public static AttrEnum getAttrEnum(int id) {
        return fightAttrEnumMap.get(id);
    }
}
