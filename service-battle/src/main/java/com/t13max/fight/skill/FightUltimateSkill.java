package com.t13max.fight.skill;

import com.t13max.fight.context.FightContext;
import com.t13max.fight.hero.FightHero;
import com.t13max.fight.attr.FightAttrEnum;
import com.t13max.fight.event.AttributeUpdateEvent;
import com.t13max.template.temp.TemplateSkill;

/**
 * 终极技能
 *
 * @author: t13max
 * @since: 17:46 2024/5/27
 */
public class FightUltimateSkill extends AbstractSkill {

    public FightUltimateSkill(FightContext fightContext, long ownerId, TemplateSkill templateSkill) {
        super(fightContext, ownerId, templateSkill);
    }

    @Override
    public boolean available() {
        FightHero fightHero = this.fightContext.getFightMatch().getFightHero(this.ownerId);
        if (fightHero == null) {
            return false;
        }
        Double finalAttr = fightHero.getFightAttrManager().getFinalAttr(FightAttrEnum.CHARGE);
        return finalAttr >= templateSkill.getCollDown();
    }

    @Override
    public boolean consumeCost() {
        FightHero fightHero = this.fightContext.getFightMatch().getFightHero(this.ownerId);
        if (fightHero == null) {
            return false;
        }
        Double oldValue = fightHero.getFightAttrManager().getFinalAttr(FightAttrEnum.CHARGE);
        fightHero.getFightAttrManager().modifyValueAttr(FightAttrEnum.CHARGE, oldValue, false);
        this.fightContext.getFightEventBus().postEvent(new AttributeUpdateEvent(this.ownerId, this.ownerId, FightAttrEnum.CHARGE, oldValue, -oldValue, false));
        return true;
    }

}
