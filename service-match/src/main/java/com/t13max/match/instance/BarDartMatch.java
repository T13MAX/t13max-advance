package com.t13max.match.instance;

import com.t13max.match.interfaces.IMatch;
import com.t13max.match.player.BarDartMatchPlayer;

import java.util.*;

/**
 * 匹配样例
 *
 * @author: t13max
 * @since: 14:13 2024/4/28
 */
public class BarDartMatch implements IMatch<BarDartMatchPlayer> {

    @Override
    public void matchSuccess(List<BarDartMatchPlayer> matchPlayerList) {

    }

    @Override
    public void handleTimeout() {

    }

    @Override
    public void matchRobot(int roomType, List<BarDartMatchPlayer> matchPlayerList) {

    }

    @Override
    public void cancelMatch(long playerId) {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public long getTimeoutMills() {
        return Integer.MAX_VALUE;
    }

}
