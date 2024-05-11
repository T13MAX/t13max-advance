package com.t13max.fight.log;

import com.t13max.fight.attr.FightAttrEnum;
import com.t13max.fight.event.AttributeUpdateEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

/**
 * @author: t13max
 * @since: 17:15 2024/4/22
 */
@Data
@NoArgsConstructor
public class AttrUpdateLog {

    private long generateHeroId;

    private long targetHeroId;

    private FightAttrEnum attrEnum;

    private double oldValue;

    private double delta;

    private boolean add;

    public AttrUpdateLog(AttributeUpdateEvent attributeUpdateEvent) {
        this.generateHeroId = attributeUpdateEvent.getGenerateHeroId();
        this.targetHeroId = attributeUpdateEvent.getTargetHeroId();
        this.attrEnum = attributeUpdateEvent.getAttrEnum();
        this.oldValue = attributeUpdateEvent.getOldValue();
        this.delta = attributeUpdateEvent.getDelta();
        this.add = attributeUpdateEvent.isAdd();
    }

    @Override
    public String toString() {
        String addStr = add ? LogConst.ADD_STR : LogConst.SUB_STR;
        return targetHeroId + "的" + attrEnum.toString() + addStr + FightLogManager.DF.format(delta);
    }
}
