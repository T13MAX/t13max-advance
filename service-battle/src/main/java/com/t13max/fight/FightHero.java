package com.t13max.fight;

import com.t13max.fight.attr.FightAttrManager;
import com.t13max.fight.buff.BuffManager;
import com.t13max.fight.impact.IImpact;
import com.t13max.fight.impact.ImpactFactory;
import com.t13max.fight.member.FightMember;
import com.t13max.fight.skill.FightSkill;
import com.t13max.fight.skill.SkillManager;
import com.t13max.template.helper.HeroHelper;
import com.t13max.template.helper.SkillHelper;
import com.t13max.template.manager.TemplateManager;
import com.t13max.template.temp.TemplateHero;
import com.t13max.template.temp.TemplateSkill;
import com.t13max.util.Log;
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

    private LifecycleObserver lifecycleObserver;

    private transient FightContext fightContext;

    public FightHero() {
    }

    public static final FightHero createFightHero(FightContext fightContext, long id, int templateId, FightMember fightMember) {
        FightHero fightHero = new FightHero();

        try {
            fightHero.fightContext = fightContext;
            fightHero.id = id;
            fightHero.fightMember = fightMember;
            fightHero.templateId = templateId;
            fightHero.fightAttrManager = new FightAttrManager(fightHero);
            fightHero.buffManager = new BuffManager(fightHero);
            fightHero.skillManager = new SkillManager(fightHero);
            fightHero.lifecycleObserver = new LifecycleObserver(fightHero);
            fightContext.getFightEventBus().register(fightHero.lifecycleObserver);

        } catch (Exception e) {
            Log.battle.error("英雄创建失败, error={}", e.getMessage());
            //异常处理
        }

        return fightHero;
    }

    public ActionArgs runWithAI() {
        ActionArgs actionArgs = new ActionArgs(this.fightMember.getUid(), this.id);
        HeroHelper heroHelper = TemplateManager.inst().helper(HeroHelper.class);
        TemplateHero template = heroHelper.getTemplate(this.getTemplateId());
        actionArgs.setSkillId(template.getSkill1());
        List<FightHero> heroList  = this.fightContext.getFightMatch().getTargetHeroList(this.isAttacker());
        FightHero random = RandomUtil.random(heroList);
        actionArgs.setTargetIds(Collections.singletonList(random.getId()));
        return actionArgs;
    }

    public boolean isCanDoAction() {
        //暂时先只检测死没死
        return fightAttrManager.isAlive();
    }

    public void emitSkill(int skillId, List<Long> targetIds, FightMatch fight) {
        FightSkill skill = this.getSkillManager().getSkill(skillId);
        if (skill == null) {
            log.error("英雄没这个技能! heroId={}, skillId={}", this.id, skillId);
            return;
        }
        SkillHelper skillHelper = TemplateManager.inst().helper(SkillHelper.class);
        TemplateSkill template = skillHelper.getTemplate(skillId);
        if (template == null) {
            log.error("技能模板为空! skillId={}", skillId);
            return;
        }

        if (checkTargetValid(template, targetIds)) {
            Log.battle.error("目标不合法, skillId={}, targetIds={}", skillId, targetIds);
            return;
        }

        FightTimeMachine fightTimeMachine = fightContext.getFightTimeMachine();

        int[] toSelfImpacts = template.getToSelfImpacts();
        String[] selfParams = template.getSelfParams();
        for (int i = 0; i < toSelfImpacts.length; i++) {
            int impactId = toSelfImpacts[i];
            IImpact impact = ImpactFactory.createImpact(fightContext,this.getId(), skillId, selfParams[i], impactId, this.isAttacker(), 0, fight.getRound(), Collections.singletonList(this.id));
            if (impact != null) {
                fightTimeMachine.addImpactToTimeLine(impact);
            }
        }

        int[] toOtherImpacts = template.getToOtherImpacts();
        String[] otherParams = template.getOtherParams();
        for (int i = 0; i < toOtherImpacts.length; i++) {
            int impactId = toOtherImpacts[i];
            IImpact impact = ImpactFactory.createImpact(fightContext,this.getId(), skillId, otherParams[i], impactId, this.isAttacker(), 0, fight.getRound(), targetIds);
            if (impact != null) {
                fightTimeMachine.addImpactToTimeLine(impact);
            }
        }

    }

    /**
     * 校验目标合法性 暂不校验
     *
     * @Author t13max
     * @Date 15:05 2024/5/24
     */
    private boolean checkTargetValid(TemplateSkill template, List<Long> targetIds) {
        return true;
    }

    public boolean isDie() {
        return !this.fightAttrManager.isAlive();
    }

    public boolean isAttacker() {
        return this.fightMember.isAttacker();
    }
}
