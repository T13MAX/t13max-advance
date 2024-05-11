package com.t13max.fight.event;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author: t13max
 * @since: 18:35 2024/4/15
 */
@Getter
public class ReadyToSubHpEvent extends AbstractEvent {

    private long generateHeroId;

    private Map<Long, Double> damageMap;

    public ReadyToSubHpEvent(long generateHeroId, Map<Long, Double> damageMap) {
        super(FightEventEnum.READY_TO_SUB_HP);
        this.damageMap = damageMap;
        this.generateHeroId = generateHeroId;
    }

}
