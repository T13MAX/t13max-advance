package com.t13max.match.player;

import com.t13max.match.consts.MatchEnum;
import com.t13max.match.interfaces.IMatchPlayer;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: t13max
 * @since: 15:43 2024/5/9
 */
@Getter
@Setter
public class AbstractMatchPlayer implements IMatchPlayer {

    //玩家id
    protected long playerId;

    //分数 根据一定规则计算出的玩家的水平
    protected double score;
    //分段 按分段匹配
    protected int scope;
    //匹配类型
    protected MatchEnum matchEnum;
    //开始匹配时间
    protected long beginMatchMills = System.currentTimeMillis();
    //缓存 不为空则是直接匹配机器人
    protected boolean directRobot;
}
