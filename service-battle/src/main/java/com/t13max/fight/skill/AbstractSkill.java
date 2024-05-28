package com.t13max.fight.skill;

import com.t13max.fight.FightContext;
import com.t13max.template.temp.TemplateSkill;
import com.t13max.util.UuidUtil;
import lombok.Getter;

/**
 * 抽象技能类
 * 技能可能分很多种
 *
 * @author: t13max
 * @since: 17:43 2024/5/27
 */
public abstract class AbstractSkill implements IFightSkill {

    protected long ownerId;

    protected FightContext fightContext;

    @Getter
    protected TemplateSkill templateSkill;

    public AbstractSkill(FightContext fightContext, long ownerId, TemplateSkill templateSkill) {
        this.fightContext = fightContext;
        this.ownerId = ownerId;
        this.templateSkill = templateSkill;
    }


    @Override
    public int getSkillId() {
        return templateSkill.getId();
    }
}
