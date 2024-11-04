package com.t13max.match.interfaces;


import com.t13max.match.consts.MatchEnum;

/**
 * 匹配用玩家信息
 * set方法是给fastjson反射用的 用来模糊反序列化(传接口 不传实现类)
 *
 * @author: t13max
 * @since: 14:14 2024/4/28
 */
public interface IMatchPlayer {

    long getPlayerId();

    void setPlayerId(long playerId);

    void setScore(double score);

    double getScore();

    int getScope();

    void setScope(int score);

    MatchEnum getMatchEnum();

    void setMatchEnum(MatchEnum matchEnum);

    long getBeginMatchMills();

    void setBeginMatchMills(long mills);

    boolean isDirectRobot();

    void setDirectRobot(boolean directRobot);
}
