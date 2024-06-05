package com.t13max.fight.impact;

import com.t13max.fight.context.FightContext;
import com.t13max.fight.event.AbstractEventListener;
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
