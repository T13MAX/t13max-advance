package com.t13max.match.player;

import com.t13max.match.consts.MatchEnum;
import lombok.Data;

/**
 * 酒吧匹配玩家信息
 *
 * @author: t13max
 * @since: 14:14 2024/4/28
 */
@Data
public class BarDartMatchPlayer extends AbstractMatchPlayer {

    public BarDartMatchPlayer() {
        matchEnum = MatchEnum.BAR_DART;
    }
}
