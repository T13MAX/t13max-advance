package com.t13max.fight.action;

import com.t13max.fight.FightHero;
import com.t13max.fight.FightImpl;
import com.t13max.fight.FightTimeMachine;
import com.t13max.fight.buff.BuffBoxImpl;
import com.t13max.fight.buff.BuffFactory;
import com.t13max.fight.buff.IBuffBox;
import com.t13max.fight.buff.effect.Buff_Effect_2_Element;
import com.t13max.fight.buff.effect.IBuffEffect;
import com.t13max.fight.buff.effect.element.ElementEnum;
import com.t13max.fight.damage.CommonDamageCalculator;
import com.t13max.fight.damage.ElementDamageCalculator;
import com.t13max.fight.event.ReadyToSubHpEvent;
import com.t13max.template.temp.TemplateSkill;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 带属性的攻击
 *
 * @author: t13max
 * @since: 10:41 2024/4/11
 */
@Log4j2
public class Action_3_ElementAttack extends AbstractAction {

    private ElementEnum element;

    private double amount;

    private int buffId;

    @Override
    public void handleCreate() {
        super.handleCreate();
        TemplateSkill template = TemplateSkill.getTemplate(getSkillId());
        if (template == null) {
            log.error("TemplateSkill为空, id={}", getSkillId());
            return;
        }
        String params = template.getParams();
        String[] split = params.split(",");
        if (split.length != 3) {
            log.error("split.length != 3, id={}", getSkillId());
            return;
        }
        this.element = ElementEnum.getElement(Integer.parseInt(split[0]));
        this.amount = Integer.parseInt(split[1]);
        this.buffId = Integer.parseInt(split[2]);
    }

    @Override
    public void onTimeIsUp() {
        FightTimeMachine fightTimeMachine = this.getFightTimeMachine();
        FightImpl fight = fightTimeMachine.getFight();
        FightHero fightHero = fight.getFightHero(this.getGenerator(), this.isAttacker());
        List<Long> targetHeroIds = this.getTargetHeroIds();

        Map<Long, Double> damageMap = new HashMap<>();

        for (Long targetHeroId : targetHeroIds) {
            FightHero targetHero = fight.getFightHero(targetHeroId, !this.isAttacker());
            double damage;

            Buff_Effect_2_Element effectElement = this.getEffectElement(targetHero);
            if (effectElement != null) {
                ElementEnum targetElementEnum = effectElement.getElementEnum();
                double targetAmount = effectElement.getAmount();
                damage = new ElementDamageCalculator(element, amount, targetElementEnum, targetAmount, fightHero, targetHero).calcDamage();
                this.amount -= Math.min(amount, targetAmount);
            } else {
                //没有 就是挂元素+普通攻击
                damage = new CommonDamageCalculator(fightHero, targetHero).calcDamage();
                BuffBoxImpl buffBoxImpl = BuffFactory.createBuffBoxImpl(fightHero, Collections.singletonList(buffId));
                fightHero.getBuffManager().addBuff(buffBoxImpl);
            }

            damageMap.put(targetHeroId, damage);
        }

        postEvent(new ReadyToSubHpEvent(fightHero.getId(), damageMap));
    }

    private Buff_Effect_2_Element getEffectElement(FightHero targetHero) {
        for (IBuffBox buffBox : targetHero.getBuffManager().getBuffMap().values()) {
            for (IBuffEffect buffEffect : buffBox.getBuffEffects()) {
                if (buffEffect instanceof Buff_Effect_2_Element effect_2_element) {
                    return effect_2_element;
                }
            }
        }
        return null;
    }

}
