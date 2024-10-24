package com.t13max.fight.event;

import com.t13max.fight.context.FightContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 战斗事件总线
 *
 * @author: t13max
 * @since: 16:49 2024/4/15
 */
public class FightEventBus {

    private final FightContext fightContext;

    private final Map<FightEventEnum, LinkedList<IFightEventListener>> listenersMap = new ConcurrentHashMap<>();

    public FightEventBus(FightContext fightContext) {
        this.fightContext = fightContext;
    }

    public void register(List<IFightEventListener> eventListenerList) {
        for (IFightEventListener eventListener : eventListenerList) {
            register(eventListener);
        }
    }

    public void register(IFightEventListener eventListener) {
        Set<FightEventEnum> interestedEvent = eventListener.getInterestedEvent();
        for (FightEventEnum fightEventEnum : interestedEvent) {
            LinkedList<IFightEventListener> listeners = listenersMap.computeIfAbsent(fightEventEnum, k -> new LinkedList<>());
            listeners.add(eventListener);
            listeners.sort(Comparator.comparingInt(IFightEventListener::getPriority));
        }
    }

    public void unregister(IFightEventListener eventListener) {
        Set<FightEventEnum> interestedEvent = eventListener.getInterestedEvent();
        for (FightEventEnum fightEventEnum : interestedEvent) {
            LinkedList<IFightEventListener> listeners = listenersMap.get(fightEventEnum);
            if (listeners == null) {
                continue;
            }
            listeners.remove(eventListener);
        }
    }

    public void postEvent(IFightEvent event) {
        FightEventEnum fightEventEnum = event.getFightEventEnum();

        LinkedList<IFightEventListener> listeners = listenersMap.get(fightEventEnum);
        if (listeners == null) {
            return;
        }

        for (IFightEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
