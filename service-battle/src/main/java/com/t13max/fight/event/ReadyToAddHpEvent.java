package com.t13max.fight.event;

import com.t13max.fight.attr.AttrUpdateReason;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: t13max
 * @since: 18:35 2024/4/15
 */
@Getter
public class ReadyToAddHpEvent extends AbstractEvent {

    private long generateHeroId;

    private long targetHeroId;

    private double value;

    @Setter
    private AttrUpdateReason attrUpdateReason = AttrUpdateReason.DEF;

    public ReadyToAddHpEvent(long generateHeroId, long targetHeroId, double value) {
        super(FightEventEnum.READY_TO_ADD_HP);
        this.value = value;
        this.generateHeroId = generateHeroId;
        this.targetHeroId = targetHeroId;
    }

    public ReadyToAddHpEvent(long generateHeroId, long targetHeroId, double value, AttrUpdateReason attrUpdateReason) {
        super(FightEventEnum.READY_TO_ADD_HP);
        this.value = value;
        this.generateHeroId = generateHeroId;
        this.targetHeroId = targetHeroId;
        this.attrUpdateReason = attrUpdateReason;
    }

}
