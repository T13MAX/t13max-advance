package com.t13max.fight.skill;

import com.t13max.fight.context.FightContext;
import com.t13max.template.temp.TemplateSkill;

/**
 * 被动
 *
 * @author: t13max
 * @since: 17:46 2024/5/27
 */
public class FightPassiveSkill extends AbstractSkill {

    public FightPassiveSkill(FightContext fightContext, long ownerId, TemplateSkill templateSkill) {
        super(fightContext, ownerId, templateSkill);
    }

    @Override
    public boolean available() {
        return true;
    }

    @Override
    public boolean consumeCost() {
        return true;
    }

}
