package com.t13max.match.interfaces;


import java.util.List;

/**
 * @author: t13max
 * @since: 14:12 2024/4/28
 */
public interface IMatch<M extends IMatchPlayer> {

    //匹配成功后逻辑
    void matchSuccess(List<M> matchPlayerList);

    //处理超时
    void handleTimeout();

    //匹配机器人逻辑
    void matchRobot(int roomType, List<M> matchPlayerList);

    //获取匹配超时时间
    long getTimeoutMills();

    void cancelMatch(long playerId);

    void shutdown();
}
