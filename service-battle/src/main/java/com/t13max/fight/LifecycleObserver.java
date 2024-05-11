package com.t13max.fight;

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
            case UNIT_DEAD:
                UnitDeadEvent unitDeadEvent = (UnitDeadEvent) event;
                unitDead(unitDeadEvent);
                break;
            case SMALL_ROUND_END:
                break;
            case ATTRIBUTE_UPDATE:
                break;
            case READY_TO_ADD_HP:
                break;
            case READY_TO_SUB_HP:
                ReadyToSubHpEvent readyToSubHpEvent = (ReadyToSubHpEvent) event;
                handleSubHp(readyToSubHpEvent);
                break;
            default:

        }
    }

    private void unitDead(UnitDeadEvent unitDeadEvent) {
        if (!unitDeadEvent.getDeadList().contains(this.fightHero.getId())) {
            return;
        }
        this.fightHero.getFight().unitDead(this.fightHero);
    }

    private void handleSubHp(ReadyToSubHpEvent readyToSubHpEvent) {

        Double oldValue = fightHero.getFightAttrManager().getFinalAttr(FightAttrEnum.CUR_HP);

        Double subValue = readyToSubHpEvent.getDamageMap().get(fightHero.getId());

        if (subValue == null) {
            return;
        }
        fightHero.getFightAttrManager().subHp(subValue);

        fightHero.getFight().getFightEventBus().postEvent(new AttributeUpdateEvent(readyToSubHpEvent.getGenerateHeroId(), fightHero.getId(), FightAttrEnum.CUR_HP, oldValue, subValue, false));

        if (fightHero.isDie()) {
            fightHero.getFight().getFightEventBus().postEvent(new UnitDeadEvent(Collections.singletonList(this.fightHero.getId())));
        }
    }


}
