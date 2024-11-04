package com.t13max.match.consts;

import com.t13max.match.instance.BarDartMatch;
import com.t13max.match.interfaces.IMatch;
import com.t13max.match.interfaces.IMatchPlayer;
import com.t13max.match.player.BarDartMatchPlayer;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 匹配类型枚举
 *
 * @author: t13max
 * @since: 14:12 2024/4/28
 */
@Getter
public enum MatchEnum {

    BAR_DART(1, 2, new BarDartMatch(), BarDartMatchPlayer.class),

    ;

    private static Map<Integer, MatchEnum> DATA_MAP = new HashMap<>();

    static {
        for (MatchEnum matchEnum : values()) {
            DATA_MAP.put(matchEnum.getId(), matchEnum);
        }
    }

    private int id;

    //这个匹配需要的人数
    private int num;

    private final IMatch<IMatchPlayer> match;

    private final Class<? extends IMatchPlayer> infoClass;

    <M extends IMatchPlayer> MatchEnum(int id, int num, IMatch<M> match, Class<? extends IMatchPlayer> infoClass) {
        this.id = id;
        this.num = num;
        this.match = (IMatch<IMatchPlayer>) match;
        this.infoClass = infoClass;
    }

    /**
     * 根据id获取MatchEnum
     *
     * @Author t13max
     * @Date 14:23 2024/4/28
     */
    public static MatchEnum getMatchEnum(int id) {
        return DATA_MAP.get(id);
    }

}
