package com.t13max.game.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author t13max
 * @since 14:58 2024/11/4
 */
@Getter
public enum RankEnum {


    ;
    final int id;
    final int show;
    final int num;
    final RankSortEnum sortEnum;
    final String desc;

    RankEnum(int id, int show, int num, RankSortEnum sortEnum, String desc) {
        this.id = id;
        this.show = show;
        this.num = num;
        this.sortEnum = sortEnum;
        this.desc = desc;
    }


    private final static Map<Integer, RankEnum> DATA_MAP = new HashMap<>();

    static {
        for (RankEnum rankEnum : values()) {
            DATA_MAP.put(rankEnum.getId(), rankEnum);
        }
    }

    public static RankEnum getRankEnum(int id) {
        return DATA_MAP.get(id);
    }
}
