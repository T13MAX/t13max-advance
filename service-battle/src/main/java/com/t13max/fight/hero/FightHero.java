package com.t13max.fight.hero;

import battle.entity.FightHeroInfoPb;
import com.t13max.fight.hero.ai.IHeroAI;
import com.t13max.fight.hero.ai.SimpleHeroAI;
import com.t13max.fight.member.IFightMember;
import com.t13max.fight.moveBar.MoveBarUnit;
import com.t13max.fight.msg.DoActionArgs;
import com.t13max.fight.FightContext;
import com.t13max.fight.FightMatch;
import com.t13max.fight.FightTimeMachine;
import com.t13max.fight.attr.FightAttrManager;
import com.t13max.fight.buff.BuffManager;
import com.t13max.fight.impact.IImpact;
import com.t13max.fight.impact.ImpactFactory;
import com.t13max.fight.skill.IFightSkill;
import com.t13max.fight.skill.SkillManager;
import com.t13max.game.exception.BattleException;
import com.t13max.template.helper.SkillHelper;
import com.t13max.template.manager.TemplateManager;
import com.t13max.template.temp.TemplateSkill;
import com.t13max.util.Log;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.List;

/**
 * @author: t13max
 * @since: 14:57 2024/4/10
 */
@Getter
@Log4j2
public class FightHero {

    private long id;

    private IFightMember fightMember;

    private int templateId;

    private FightAttrManager fightAttrManager;

    private BuffManager buffManager;

    private SkillManager skillManager;

    private LifecycleObserver lifecycleObserver;

    private IHeroAI heroAI;

    @Setter
    private volatile boolean autoAction;

    private transient FightContext fightContext;

    public FightHero() {
    }

    public static FightHero createFightHero(FightContext fightContext, long id, int templateId, IFightMember fightMember) {
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
            throw new BattleException("英雄创建失败, heroId=" + templateId);
        }

        return fightHero;
    }

    public DoActionArgs runWithAI() {
        if (this.heroAI == null) {
            //暂时先就使用简单AI
            heroAI = new SimpleHeroAI(this);
        }

        return heroAI.doAction();
    }

    public boolean isCanDoAction() {
        //暂时先只检测死没死
        return fightAttrManager.isAlive();
    }

    public void emitSkill(int skillId, List<Long> targetIds, FightMatch fight) {
        IFightSkill fightSkill = this.getSkillManager().getFightSkill(skillId);
        if (fightSkill == null) {
            log.error("英雄没这个技能! heroId={}, skillId={}", this.id, skillId);
            return;
        }
        SkillHelper skillHelper = TemplateManager.inst().helper(SkillHelper.class);
        TemplateSkill template = skillHelper.getTemplate(skillId);
        if (template == null) {
            log.error("技能模板为空! skillId={}", skillId);
            return;
        }

        if (!checkTargetValid(template, targetIds)) {
            Log.battle.error("目标不合法, skillId={}, targetIds={}", skillId, targetIds);
            return;
        }

        FightTimeMachine fightTimeMachine = fightContext.getFightTimeMachine();

        int[] toSelfImpacts = template.getToSelfImpacts();
        String[] selfParams = template.getSelfParams();
        for (int i = 0; i < toSelfImpacts.length; i++) {
            int impactId = toSelfImpacts[i];
            IImpact impact = ImpactFactory.createImpact(fightContext, this.getId(), skillId, selfParams[i], impactId, this.isAttacker(), 0, fight.getRound(), Collections.singletonList(this.id));
            if (impact != null) {
                fightTimeMachine.addImpactToTimeLine(impact);
            }
        }

        int[] toOtherImpacts = template.getToOtherImpacts();
        String[] otherParams = template.getOtherParams();
        for (int i = 0; i < toOtherImpacts.length; i++) {
            int impactId = toOtherImpacts[i];
            IImpact impact = ImpactFactory.createImpact(fightContext, this.getId(), skillId, otherParams[i], impactId, this.isAttacker(), 0, fight.getRound(), targetIds);
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

    public FightHeroInfoPb buildFightHeroInfoPb() {
        FightHeroInfoPb.Builder builder = FightHeroInfoPb.newBuilder();
        builder.setHeroId(this.id);
        builder.setTemplateId(this.templateId);
        MoveBarUnit moveBarUnit = this.fightContext.getFightMatch().getActionMoveBar().getUnit(this.id);
        if (moveBarUnit != null) {
            builder.setMoveBar(moveBarUnit.buildBattleMoveBar());
        }
        return builder.build();
    }
}
