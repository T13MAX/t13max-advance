package com.t13max.game.feature.active.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


/**
 * @author: t13max
 * @since: 15:38 2024/6/6
 */
@Getter
public enum ActOpenEnum {
    //固定 周几循环
    WEEK_DAY(1),

    //解析日期
    DATE(8);

    private final int id;

    private static final Map<Integer, ActOpenEnum> DATA_MAP = new HashMap<>();

    static {
        for (ActOpenEnum actOpenEnum : values()) {
            DATA_MAP.put(actOpenEnum.getId(), actOpenEnum);
        }
    }
    ActOpenEnum(int id) {
        this.id = id;
    }

    public static ActOpenEnum getActOpenEnum(int id) {
        return DATA_MAP.get(id);
    }

}
