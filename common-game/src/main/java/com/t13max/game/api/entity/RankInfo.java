package com.t13max.game.api.entity;

import com.t13max.game.enums.RankEnum;
import com.t13max.game.rank.RankCondition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 一个更新单元
 *
 * @author t13max
 * @since 15:34 2024/10/31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankInfo {

    //服务器id
    private int serverId;
    //玩家id
    private long pid;
    //排行榜类型
    private RankEnum rankEnum;
    //分数
    private long score;
    //排序用key条件
    private List<RankCondition> conditions;
}
