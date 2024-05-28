package com.t13max.fight.hero.ai;

import com.t13max.fight.hero.FightHero;

/**
 * @author: t13max
 * @since: 15:30 2024/5/28
 */
public abstract class AbstractHeroAI implements IHeroAI{

    protected FightHero owner;

    public AbstractHeroAI(FightHero fightHero) {
        this.owner = fightHero;
    }
}
