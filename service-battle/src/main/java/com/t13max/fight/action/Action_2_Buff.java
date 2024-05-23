package com.t13max.fight.action;

import com.t13max.fight.FightHero;
import com.t13max.fight.FightImpl;
import com.t13max.fight.FightTimeMachine;
import com.t13max.fight.buff.BuffBoxImpl;
import com.t13max.fight.buff.BuffFactory;
import com.t13max.template.temp.TemplateSkill;
import lombok.extern.log4j.Log4j2;

import java.util.*;

/**
 * 挂buff
 *
 * @author: t13max
 * @since: 10:41 2024/4/11
 */
@Log4j2
public class Action_2_Buff extends AbstractAction {

    @Override
    public void handleCreate() {
        super.handleCreate();
    }

    @Override
    public void onTimeIsUp() {
        FightTimeMachine fightTimeMachine = this.getFightTimeMachine();
        FightImpl fight = fightTimeMachine.getFight();

        TemplateSkill template = TemplateSkill.getTemplate(getSkillId());
        if (template == null) {
            log.error("TemplateSkill为空, skillId={}", getSkillId());
            return;
        }

        List<Integer> intListParam = template.getIntListParam();
        if (intListParam.isEmpty()) {
            log.error("intListParam为空, skillId={}", getSkillId());
            return;
        }

        for (Long targetHeroId : this.getTargetHeroIds()) {
            FightHero fightHero = fight.getFightHero(targetHeroId, !this.isAttacker());
            if (fightHero == null) {
                continue;
            }

            BuffBoxImpl buffBoxImpl = BuffFactory.createBuffBoxImpl(fightHero, intListParam);
            fightHero.getBuffManager().addBuff(buffBoxImpl);
        }
    }


}
