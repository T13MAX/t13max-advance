package com.t13max.game.event;

/**
 * @author: t13max
 * @since: 14:58 2024/5/29
 */
public class AbstractGameEvent implements IGameEvent {

    protected GameEventEnum gameEventEnum;

    @Override
    public GameEventEnum getGameEventEnum() {
        return gameEventEnum;
    }
}
