package com.t13max.fight.skill;

import com.t13max.fight.FightHero;
import com.t13max.fight.event.AbstractEventListener;
import com.t13max.fight.event.FightEventEnum;
import com.t13max.fight.event.IFightEvent;
import com.t13max.fight.event.IFightEventListener;
import com.t13max.game.exception.BattleException;
import com.t13max.template.helper.HeroHelper;
import com.t13max.template.helper.SkillHelper;
import com.t13max.template.manager.TemplateManager;
import com.t13max.template.temp.TemplateHero;
import com.t13max.template.temp.TemplateSkill;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: t13max
 * @since: 15:56 2024/4/15
 */
public class SkillManager extends AbstractEventListener {

    private FightHero owner;

    private Map<Integer, IFightSkill> skillMap = new HashMap<>();

    public SkillManager(FightHero owner) {
        this.owner = owner;

        HeroHelper heroHelper = TemplateManager.inst().helper(HeroHelper.class);
        SkillHelper skillHelper = TemplateManager.inst().helper(SkillHelper.class);
        TemplateHero templateHero = heroHelper.getTemplate(owner.getTemplateId());
        if (templateHero == null) {
            throw new BattleException("TemplateHero为空, id=" + owner.getTemplateId());
        }


        for (int skillId : templateHero.getSkill()) {
            TemplateSkill templateSkill = skillHelper.getTemplate(skillId);
            if (templateSkill == null) {
                throw new BattleException("TemplateSkill为空, id=" + skillId);
            }
            IFightSkill fightSkill = SkillFactory.createFightSkill(owner.getFightContext(), owner.getId(), templateSkill);

            skillMap.put(fightSkill.getSkillId(), fightSkill);
        }

        subscribeEvent(FightEventEnum.SMALL_ROUND_BEGIN);
    }

    public IFightSkill getFightSkill(int skillId) {
        return skillMap.get(skillId);
    }

    @Override
    public void onEvent(IFightEvent event) {
        switch (event.getFightEventEnum()) {
            case SMALL_ROUND_BEGIN -> {
                for (IFightSkill fightSkill : this.skillMap.values()) {
                    if (fightSkill instanceof FightNormalSkill fightNormalSkill) {
                        fightNormalSkill.decreaseCoolDown();
                    }
                }
            }
        }
    }
}
