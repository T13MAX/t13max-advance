package com.t13max.fight.impact;

import com.t13max.fight.FightTimeMachine;
import com.t13max.fight.event.AbstractEventListener;
import com.t13max.fight.event.IFightEventListener;
import lombok.Data;

import java.util.List;

/**
 * @author: t13max
 * @since: 18:00 2024/4/15
 */
@Data
public abstract class AbstractImpact extends AbstractEventListener implements IImpact {

    private FightTimeMachine fightTimeMachine;

    private ImpactEnum impactEnum;

    private int skillId;

    private long generator;

    private List<Long> targetHeroIds;

    private boolean attacker;

    private int delayTime;

    private int generateRound;

}
