package com.t13max.game.event;

import com.t13max.game.manager.ManagerBase;

import java.util.*;

/**
 * 这里一套 battle一套 因为这里后续要加入redis发布订阅 弄成跨进程事件系统
 *
 * @author: t13max
 * @since: 14:56 2024/5/29
 */
public class GameEventBus extends ManagerBase {

    private Map<GameEventEnum, LinkedList<IGameEventListener>> listenersMap = new HashMap<>();

    public static GameEventBus inst() {
        return inst(GameEventBus.class);
    }

    public void register(List<IGameEventListener> eventListenerList) {
        for (IGameEventListener eventListener : eventListenerList) {
            register(eventListener);
        }
    }

    public void register(IGameEventListener eventListener) {
        Set<GameEventEnum> interestedEvent = eventListener.getInterestedEvent();
        for (GameEventEnum GameEventEnum : interestedEvent) {
            LinkedList<IGameEventListener> listeners = listenersMap.computeIfAbsent(GameEventEnum, k -> new LinkedList<>());
            listeners.add(eventListener);
            listeners.sort(Comparator.comparingInt(IGameEventListener::getPriority));
        }
    }

    public void unregister(IGameEventListener eventListener) {
        Set<GameEventEnum> interestedEvent = eventListener.getInterestedEvent();
        for (GameEventEnum GameEventEnum : interestedEvent) {
            LinkedList<IGameEventListener> listeners = listenersMap.get(GameEventEnum);
            if (listeners == null) {
                continue;
            }
            listeners.remove(eventListener);
        }
    }

    public void postEvent(IGameEvent event) {
        GameEventEnum GameEventEnum = event.getGameEventEnum();

        LinkedList<IGameEventListener> listeners = listenersMap.get(GameEventEnum);
        if (listeners == null) {
            return;
        }

        for (IGameEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
