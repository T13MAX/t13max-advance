package com.t13max.fight.buff.condition;

import com.t13max.fight.event.FightEventEnum;
import com.t13max.fight.event.IFightEvent;
import com.t13max.fight.event.SmallRoundBeginEvent;


/**
 * @author: t13max
 * @since: 14:02 2024/5/27
 */
public class Condition_1_RoundCount extends AbstractEventCondition {

    private int count;

    private final int targetCount;

    private int lastRound = 0;

    public Condition_1_RoundCount(String param) {
        this.targetCount = Integer.parseInt(param);
        subEvent(FightEventEnum.SMALL_ROUND_BEGIN);
    }

    @Override
    public ConditionEnum getConditionEnum() {
        return ConditionEnum.ROUND_COUNT;
    }

    @Override
    public boolean isMatch(IFightEvent event) {

        switch (event.getFightEventEnum()) {
            case SMALL_ROUND_BEGIN -> {
                SmallRoundBeginEvent smallRoundBeginEvent = (SmallRoundBeginEvent) event;
                int round = smallRoundBeginEvent.getRound();
                if (lastRound != round) {
                    lastRound = round;
                    count++;
                }
                if (count >= targetCount) return true;
            }
        }
        return false;
    }
}
