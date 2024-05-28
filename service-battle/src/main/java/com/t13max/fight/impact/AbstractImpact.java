package com.t13max.fight.impact;

import com.t13max.fight.FightContext;
import com.t13max.fight.FightTimeMachine;
import com.t13max.fight.event.AbstractEventListener;
import com.t13max.fight.event.IFightEventListener;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: t13max
 * @since: 18:00 2024/4/15
 */
@Getter
@Setter
public abstract class AbstractImpact extends AbstractEventListener implements IImpact {

    protected transient FightContext fightContext;

    protected ImpactEnum impactEnum;

    protected int skillId;

    protected String param;

    protected long generator;

    protected List<Long> targetHeroIds;

    protected boolean attacker;

    protected int delayTime;

    protected int generateRound;

}
