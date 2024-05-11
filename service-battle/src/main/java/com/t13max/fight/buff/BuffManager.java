package com.t13max.fight.buff;

import com.t13max.fight.FightHero;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 15:27 2024/4/10
 */
@Getter
public class BuffManager {

    private FightHero owner;

    private Map<Long, IBuffBox> buffMap = new HashMap<>();

    public BuffManager(FightHero owner) {
        this.owner = owner;
    }

    public void addBuff(IBuffBox buffBox) {
        this.buffMap.put(buffBox.getId(), buffBox);
    }

    public IBuffBox removeBuff(long id) {
        return this.buffMap.remove(id);
    }
}
