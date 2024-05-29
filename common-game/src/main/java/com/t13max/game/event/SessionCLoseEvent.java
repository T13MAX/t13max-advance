package com.t13max.game.event;

import lombok.Getter;

/**
 * @author: t13max
 * @since: 14:56 2024/5/29
 */
@Getter
public class SessionCLoseEvent extends AbstractGameEvent {

    private long uuid;

    public SessionCLoseEvent(long uuid) {
        this.gameEventEnum = GameEventEnum.SESSION_CLOSE;
        this.uuid = uuid;
    }
}
