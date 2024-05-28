package com.t13max.fight.hero;

import com.t13max.fight.attr.AttrUpdateReason;
import com.t13max.fight.attr.FightAttrEnum;
import com.t13max.fight.event.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: t13max
 * @since: 10:33 2024/4/16
 */
public class LifecycleObserver implements IFightEventListener {

    private static final Set<FightEventEnum> INTEREST_EVENT;

    private FightHero fightHero;

    static {
        Set<FightEventEnum> interests = new HashSet<>();

        interests.add(FightEventEnum.UNIT_DEAD);
        interests.add(FightEventEnum.SMALL_ROUND_BEGIN);
        interests.add(FightEventEnum.SMALL_ROUND_END);
        interests.add(FightEventEnum.ATTRIBUTE_UPDATE);
        interests.add(FightEventEnum.READY_TO_ADD_HP);
        interests.add(FightEventEnum.READY_TO_SUB_HP);

        INTEREST_EVENT = interests;
    }

    public LifecycleObserver(FightHero owner) {
        this.fightHero = owner;
    }

    @Override
    public Set<FightEventEnum> getInterestedEvent() {
        return INTEREST_EVENT;
    }

    @Override
    public void onEvent(IFightEvent event) {
        switch (event.getFightEventEnum()) {
            case UNIT_DEAD -> {
                UnitDeadEvent unitDeadEvent = (UnitDeadEvent) event;
                unitDead(unitDeadEvent);
            }
            case SMALL_ROUND_END -> {
                //回合结束
            }
            case SMALL_ROUND_BEGIN -> {
                SmallRoundBeginEvent smallRoundBeginEvent = (SmallRoundBeginEvent) event;
                Double hpRecover = this.fightHero.getFightAttrManager().getFinalAttr(FightAttrEnum.HP_RECOVER);
                Double curHp = this.fightHero.getFightAttrManager().getFinalAttr(FightAttrEnum.CUR_HP);
                Double maxHp = this.fightHero.getFightAttrManager().getFinalAttr(FightAttrEnum.MAX_HP);
                double finalValue = Math.min(maxHp - curHp, hpRecover);
                if (finalValue == 0)  break;
                this.fightHero.getFightContext().getFightEventBus().postEvent(new ReadyToAddHpEvent(this.fightHero.getId(), this.fightHero.getId(), finalValue, AttrUpdateReason.SELF_CURE));
            }
            case ATTRIBUTE_UPDATE -> {
                //要是有多个变更 是不是应该最后再算一下最终属性?
                AttributeUpdateEvent attributeUpdateEvent = (AttributeUpdateEvent) event;
                this.fightHero.getFightAttrManager().calcFinalAttr();
            }
            case READY_TO_ADD_HP -> {
                ReadyToAddHpEvent readyToAddHpEvent = (ReadyToAddHpEvent) event;
                if (readyToAddHpEvent.getTargetHeroId() != fightHero.getId()) {
                    break;
                }
                Double curHp = this.fightHero.getFightAttrManager().getFinalAttr(FightAttrEnum.CUR_HP);
                double value = readyToAddHpEvent.getValue();
                if (value == 0) break;
                fightHero.getFightAttrManager().addHp(value);
                AttributeUpdateEvent attributeUpdateEvent = new AttributeUpdateEvent(readyToAddHpEvent.getGenerateHeroId(), fightHero.getId(), FightAttrEnum.CUR_HP, curHp, value, true);
                this.fightHero.getFightContext().getFightEventBus().postEvent(attributeUpdateEvent);
            }
            case READY_TO_SUB_HP -> {
                ReadyToSubHpEvent readyToSubHpEvent = (ReadyToSubHpEvent) event;
                handleSubHp(readyToSubHpEvent);
            }
            default -> {

            }

        }
    }

    private void unitDead(UnitDeadEvent unitDeadEvent) {
        if (!unitDeadEvent.getDeadList().contains(this.fightHero.getId())) {
            return;
        }
        this.fightHero.getFightContext().getFightMatch().unitDead(this.fightHero);
    }

    private void handleSubHp(ReadyToSubHpEvent readyToSubHpEvent) {

        Double oldValue = fightHero.getFightAttrManager().getFinalAttr(FightAttrEnum.CUR_HP);

        Double subValue = readyToSubHpEvent.getDamageMap().get(fightHero.getId());

        if (subValue == null) {
            return;
        }
        fightHero.getFightAttrManager().subHp(subValue);

        AttributeUpdateEvent attributeUpdateEvent = new AttributeUpdateEvent(readyToSubHpEvent.getGenerateHeroId(), fightHero.getId(), FightAttrEnum.CUR_HP, oldValue, subValue, false);
        //原因传过去
        attributeUpdateEvent.setAttrUpdateReason(readyToSubHpEvent.getAttrUpdateReason());
        this.fightHero.getFightContext().getFightEventBus().postEvent(attributeUpdateEvent);

        if (fightHero.isDie()) {
            this.fightHero.getFightContext().getFightEventBus().postEvent(new UnitDeadEvent(Collections.singletonList(this.fightHero.getId())));
        }
    }


}
