package com.t13max.fight.event;

import battle.entity.FightEventPb;
import battle.event.entity.AttributeUpdateEventPb;
import com.t13max.fight.attr.AttrUpdateReason;
import com.t13max.fight.attr.FightAttrEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: t13max
 * @since: 10:46 2024/4/16
 */
@Getter
public class AttributeUpdateEvent extends AbstractEvent implements IFightEventPackager {

    private long generateHeroId;

    private long targetHeroId;

    private FightAttrEnum attrEnum;

    private double oldValue;

    private double delta;

    private boolean add;

    @Setter
    private AttrUpdateReason attrUpdateReason = AttrUpdateReason.DEF;

    public AttributeUpdateEvent(long generateHeroId, long targetHeroId, FightAttrEnum attrEnum, double oldValue, double delta, boolean add) {
        super(FightEventEnum.ATTRIBUTE_UPDATE);
        this.attrEnum = attrEnum;
        this.oldValue = oldValue;
        this.delta = delta;
        this.add = add;
        this.generateHeroId = generateHeroId;
        this.targetHeroId = targetHeroId;
    }

    @Override
    public FightEventPb pack() {
        FightEventPb.Builder builder = FightEventPb.newBuilder();
        AttributeUpdateEventPb.Builder eventBuilder = AttributeUpdateEventPb.newBuilder();
        eventBuilder.setAttributeType(attrEnum.getId());
        eventBuilder.setDelta(delta);
        eventBuilder.setFinalValue(add ? oldValue + delta : oldValue - delta);
        eventBuilder.setGeneratorId(generateHeroId);
        eventBuilder.setHeroId(targetHeroId);
        builder.setAttributeUpdateEventPb(eventBuilder);
        return builder.build();
    }
}
