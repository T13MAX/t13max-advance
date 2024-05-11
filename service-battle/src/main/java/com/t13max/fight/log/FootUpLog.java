package com.t13max.fight.log;

import com.t13max.fight.FightHero;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 18:43 2024/4/22
 */
@Data
public class FootUpLog {

    private Map<Long, UnitLog> winMap = new HashMap<>();

    public FootUpLog(Map<Long, FightHero> winMap) {
        for (FightHero fightHero : winMap.values()) {
            this.winMap.put(fightHero.getId(), new UnitLog(fightHero));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("foot_up!");
        for (UnitLog value : winMap.values()) {
            sb.append(" ").append(value.toString());
        }
        return sb.toString();
    }
}
