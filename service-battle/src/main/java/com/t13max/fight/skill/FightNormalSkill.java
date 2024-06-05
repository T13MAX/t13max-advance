package com.t13max.fight.skill;

import com.t13max.fight.context.FightContext;
import com.t13max.template.temp.TemplateSkill;

/**
 * 普通技能
 *
 * @author: t13max
 * @since: 15:56 2024/4/15
 */

public class FightNormalSkill extends AbstractSkill {

    protected int collDown;

    public FightNormalSkill(FightContext fightContext, long ownerId, TemplateSkill templateSkill) {
        super(fightContext, ownerId, templateSkill);
    }

    @Override
    public boolean available() {
        return collDown == 0;
    }

    @Override
    public boolean consumeCost() {
        collDown += templateSkill.getCollDown();
        return false;
    }

    public void increaseCoolDown() {
        increaseCoolDown(1);
    }

    public void increaseCoolDown(int amount) {
        collDown += amount;
    }

    public void decreaseCoolDown() {
        decreaseCoolDown(1);
    }

    public void decreaseCoolDown(int amount) {
        collDown -= amount;
        if (collDown < 0) {
            collDown = 0;
        }
    }
}
