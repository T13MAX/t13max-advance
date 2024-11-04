package com.t13max.game.rank;

import com.t13max.game.enums.RankSortEnum;
import lombok.Getter;

/**
 * 排序条件
 *
 * @author t13max
 * @since 15:07 2024/11/4
 */
@Getter
public class RankCondition {

    //分数
    private final long score;
    //排序规则
    private final RankSortEnum rankSortEnum;

    public RankCondition(long score, RankSortEnum rankSortEnum) {
        this.score = score;
        this.rankSortEnum = rankSortEnum;
    }
}
