package com.t13max.fight;

import com.t13max.fight.attr.FightAttrManager;
import com.t13max.fight.buff.BuffManager;
import com.t13max.fight.impact.IImpact;
import com.t13max.fight.impact.ImpactFactory;
import com.t13max.fight.member.FightMember;
import com.t13max.fight.skill.SkillImpl;
import com.t13max.fight.skill.SkillManager;
import com.t13max.template.TemplateHero;
import com.t13max.template.TemplateSkill;
import com.t13max.util.RandomUtil;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author: t13max
 * @since: 14:57 2024/4/10
 */
@Getter
@Log4j2
public class FightHero {

    private long id;

    private FightMember fightMember;

    private int templateId;

    private FightAttrManager fightAttrManager;

    private BuffManager buffManager;

    private SkillManager skillManager;

    private FightImpl fight;

    private LifecycleObserver lifecycleObserver;

    public FightHero(long id, FightMember fightMember, int templateId, FightImpl fight) {
        this.id = id;
        this.fightMember = fightMember;
        this.templateId = templateId;
        this.fightAttrManager = new FightAttrManager(this);
        this.buffManager = new BuffManager(this);
        this.skillManager = new SkillManager(this);
        this.lifecycleObserver = new LifecycleObserver(this);
        fight.getFightEventBus().register(this.lifecycleObserver);
        this.fight = fight;
    }


    public ActionArgs runWithAI() {
        ActionArgs actionArgs = new ActionArgs(this.fightMember.getUid(), this.id);
        TemplateHero template = TemplateHero.getTemplate(this.getTemplateId());
        actionArgs.setSkillId(template.getSkill1());
        Map<Long, FightHero> targetMap = null;
        if (this.isAttacker()) {
            targetMap = this.fight.getDefender();
        } else {
            targetMap = this.fight.getAttacker();
        }
        FightHero random = RandomUtil.random(targetMap.values().stream().filter(e -> !e.isDie()).toList());
        actionArgs.setTargetIds(Collections.singletonList(random.getId()));
        return actionArgs;
    }

    public boolean isCanDoAction() {
        //暂时先只检测死没死
        return fightAttrManager.isAlive();
    }

    public void emitSkill(int skillId, List<Long> targetIds, FightImpl fight) {
        SkillImpl skill = this.getSkillManager().getSkill(skillId);
        if (skill == null) {
            log.error("英雄没这个技能! heroId={}, skillId={}", this.id, skillId);
            return;
        }

        TemplateSkill template = TemplateSkill.getTemplate(skillId);
        if (template == null) {
            log.error("技能模板为空! skillId={}", skillId);
            return;
        }
        FightTimeMachine fightTimeMachine = fight.getFightTimeMachine();
        List<Integer> toSelfImpacts = template.getToSelfImpacts();
        for (Integer impactId : toSelfImpacts) {
            IImpact impact = ImpactFactory.createImpact(this.getId(), skillId, impactId, this.isAttacker(), 0, fight.getRound(), Collections.singletonList(this.id), fightTimeMachine);
            if (impact != null) {
                fightTimeMachine.addImpactToTimeLine(impact);
            }
        }

        List<Integer> toOtherImpacts = template.getToOtherImpacts();
        for (Integer impactId : toOtherImpacts) {
            IImpact impact = ImpactFactory.createImpact(this.getId(), skillId, impactId, this.isAttacker(), 0, fight.getRound(), targetIds, fightTimeMachine);
            if (impact != null) {
                fightTimeMachine.addImpactToTimeLine(impact);
            }
        }

    }

    public boolean isDie() {
        return !this.fightAttrManager.isAlive();
    }

    public boolean isAttacker() {
        return this.fightMember.isAttacker();
    }
}
