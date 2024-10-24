package com.t13max.fight.buff;

import battle.event.entity.RemoveReason;
import com.t13max.fight.hero.FightHero;
import com.t13max.fight.event.BuffAddEvent;
import com.t13max.fight.event.BuffRemoveEvent;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 英雄buff管理器
 *
 * @author: t13max
 * @since: 15:27 2024/4/10
 */
@Getter
public class BuffManager {

    private final FightHero owner;

    private final Map<Long, IBuffBox> buffMap = new HashMap<>();

    public BuffManager(FightHero owner) {
        this.owner = owner;
    }

    public void addBuff(IBuffBox buffBox) {
        this.buffMap.put(buffBox.getId(), buffBox);
        owner.getFightContext().getFightEventBus().postEvent(new BuffAddEvent(buffBox));
    }

    public IBuffBox removeBuff(long id, RemoveReason removeReason) {
        IBuffBox buffBox = this.buffMap.remove(id);
        if (buffBox == null) {
            return null;
        }
        owner.getFightContext().getFightEventBus().postEvent(new BuffRemoveEvent(buffBox, removeReason));
        return buffBox;
    }
}
