package com.t13max.fight.event;

import com.t13max.fight.attr.AttrUpdateReason;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * @author: t13max
 * @since: 18:35 2024/4/15
 */
@Getter
public class ReadyToSubHpEvent extends AbstractEvent {

    private long generateHeroId;

    private Map<Long, Double> damageMap;

    @Setter
    private AttrUpdateReason attrUpdateReason = AttrUpdateReason.DEF;

    public ReadyToSubHpEvent(long generateHeroId, Map<Long, Double> damageMap) {
        super(FightEventEnum.READY_TO_SUB_HP);
        this.damageMap = damageMap;
        this.generateHeroId = generateHeroId;
    }

    public ReadyToSubHpEvent(long generateHeroId, Map<Long, Double> damageMap, AttrUpdateReason attrUpdateReason) {
        super(FightEventEnum.READY_TO_SUB_HP);
        this.damageMap = damageMap;
        this.generateHeroId = generateHeroId;
        this.attrUpdateReason = attrUpdateReason;
    }

}
