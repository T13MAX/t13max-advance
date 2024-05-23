package com.t13max.fight.skill;

import com.t13max.fight.FightHero;
import com.t13max.game.exception.BattleException;
import com.t13max.template.temp.TemplateHero;
import com.t13max.template.temp.TemplateSkill;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 15:56 2024/4/15
 */
public class SkillManager {

    private FightHero owner;

    private Map<Integer, FightSkill> skillMap = new HashMap<>();

    public SkillManager(FightHero owner) {
        this.owner = owner;

        TemplateHero templateHero = TemplateHero.getTemplate(owner.getTemplateId());
        if (templateHero == null) {
            throw new BattleException("TemplateHero为空, id=" + owner.getTemplateId());
        }

        TemplateSkill templateSkill1 = TemplateSkill.getTemplate(templateHero.getSkill1());
        if (templateSkill1 == null) {
            throw new BattleException("TemplateSkill为空, id=" + templateHero.getSkill1());
        }
        FightSkill skill1 = new FightSkill(templateSkill1);
        skillMap.put(skill1.getSkillId(), skill1);

        TemplateSkill templateSkill2 = TemplateSkill.getTemplate(templateHero.getSkill1());
        if (templateSkill2 != null) {
            FightSkill skill2 = new FightSkill(templateSkill2);
            skillMap.put(skill2.getSkillId(), skill2);
        }

    }

    public FightSkill getSkill(int skillId) {
        return skillMap.get(skillId);
    }
}
