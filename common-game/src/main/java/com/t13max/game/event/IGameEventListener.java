package com.t13max.game.event;

import java.util.List;
import java.util.Set;

/**
 * @author: t13max
 * @since: 14:57 2024/5/29
 */
public interface IGameEventListener {

    Set<GameEventEnum> getInterestedEvent();

    void onEvent(IGameEvent gameEvent);

    int getPriority();
}
