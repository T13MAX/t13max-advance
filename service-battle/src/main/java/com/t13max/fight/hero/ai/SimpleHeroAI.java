package com.t13max.fight.hero.ai;

import com.t13max.fight.hero.FightHero;
import com.t13max.fight.impact.SelectorEnum;
import com.t13max.fight.msg.DoActionArgs;
import com.t13max.fight.skill.IFightSkill;
import com.t13max.template.temp.TemplateSkill;

/**
 * 简单ai 凑合用
 *
 * @author: t13max
 * @since: 15:26 2024/5/28
 */
public class SimpleHeroAI extends AbstractHeroAI {

    public SimpleHeroAI(FightHero fightHero) {
        super(fightHero);
    }

    @Override
    public DoActionArgs doAction() {
        DoActionArgs doActionArgs = new DoActionArgs(this.owner.getFightMember().getId(), this.owner.getId());

        IFightSkill ranAvaSkill = this.owner.getSkillManager().getRanAvaSkill();
        TemplateSkill templateSkill = ranAvaSkill.getTemplateSkill();
        SelectorEnum otherSelector = SelectorEnum.getSelectorEnum(templateSkill.otherSelector);
        doActionArgs.setSkillId(templateSkill.getId());
        doActionArgs.setTargetIds(otherSelector.select(this.owner));
        return doActionArgs;
    }
}
