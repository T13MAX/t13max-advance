package com.t13max.fight.buff.effect;

import com.t13max.fight.attr.AttrUpdateReason;
import com.t13max.fight.attr.FightAttrEnum;
import com.t13max.fight.damage.CalcConst;
import com.t13max.fight.event.*;
import lombok.Getter;

import java.util.Collections;

/**
 * 反伤
 *
 * @author: t13max
 * @since: 15:19 2024/5/24
 */
@Getter
public class Buff_Effect_3_CounterWound extends AbstractEffect {

    //反伤的比例
    private int rate;

    @Override
    protected void onInit() {
        this.rate = Integer.parseInt(this.param);
        subscribeEvent(FightEventEnum.ATTRIBUTE_UPDATE);
    }

    @Override
    protected void doOnEvent(IFightEvent event) {

        switch (event.getFightEventEnum()) {
            case ATTRIBUTE_UPDATE -> {
                AttributeUpdateEvent attributeUpdateEvent = (AttributeUpdateEvent) event;
                if (attributeUpdateEvent.getAttrEnum() != FightAttrEnum.CUR_HP) {
                    break;
                }
                long ownerId = this.buffBox.getOwnerId();
                if (attributeUpdateEvent.getTargetHeroId() != ownerId) {
                    break;
                }
                if (attributeUpdateEvent.isAdd()) {
                    break;
                }
                if (attributeUpdateEvent.getAttrUpdateReason() == AttrUpdateReason.COUNTER_WOUND) {
                    break;
                }

                double damage = this.rate / CalcConst.MAX_RATE * attributeUpdateEvent.getDelta();

                FightEventBus fightEventBus = this.buffBox.getFightContext().getFightEventBus();
                fightEventBus.postEvent(new ReadyToSubHpEvent(ownerId, Collections.singletonMap(attributeUpdateEvent.getGenerateHeroId(), damage)));
            }
        }

    }
}
