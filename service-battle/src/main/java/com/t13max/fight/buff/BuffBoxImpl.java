package com.t13max.fight.buff;

import battle.event.entity.BuffBoxPb;
import battle.event.entity.BuffStatus;
import battle.event.entity.RemoveReason;
import com.t13max.fight.context.FightContext;
import com.t13max.fight.buff.effect.IBuffEffect;
import com.t13max.fight.event.*;
import com.t13max.game.exception.BattleException;
import com.t13max.game.util.Log;
import com.t13max.template.helper.BuffHelper;
import com.t13max.template.manager.TemplateManager;
import com.t13max.template.temp.TemplateBuff;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: t13max
 * @since: 15:57 2024/4/10
 */
@Getter
@Setter
public class BuffBoxImpl extends AbstractEventListener implements IBuffBox {

    private long id;

    private long ownerId;

    private BuffStatus buffStatus = BuffStatus.IDLE;

    //先默认全是任意一个
    private DisposedRule disposedRule = DisposedRule.ANY;

    private int buffId;

    private Set<IBuffEffect> buffEffects = new HashSet<>();

    private int layer;

    protected transient FightContext fightContext;

    @Override
    public void onEvent(IFightEvent event) {
        switch (event.getFightEventEnum()) {
            case SMALL_ROUND_END -> {
                //this.reduceLife(1);
            }
            case BUFF_EFFECT_CAN_ACTIVE -> {
                BuffEffectCanActiveEvent buffEffectCanActiveEvent = (BuffEffectCanActiveEvent) event;
                if (buffEffectCanActiveEvent.getBuffEffect().getBuffBox() != this) {
                    break;
                }
                if (buffStatus == BuffStatus.IDLE) {
                    // 任意一个效果满足生效条件, 盒子就生效
                    if (buffEffects.stream().anyMatch(buffEffect -> buffEffect.getBuffStatus() == BuffStatus.ACTIVE)) {
                        switchBuffStatus(BuffStatus.ACTIVE, null);
                    }
                }
            }
            case BUFF_EFFECT_CAN_DISPOSED -> {
                BuffEffectCanDisposedEvent buffEffectCanDisposedEvent = (BuffEffectCanDisposedEvent) event;
                if (buffEffectCanDisposedEvent.getBuffEffect().getBuffBox() != this) {
                    break;
                }
                if (buffStatus == BuffStatus.ACTIVE) {

                    // 按配置规则判断是否可消散
                    boolean canDisposed = false;

                    if (disposedRule == DisposedRule.ANY) {
                        if (buffEffects.stream().anyMatch(buffEffect -> buffEffect.getBuffStatus() == BuffStatus.DISPOSED)) {
                            canDisposed = true;
                        }
                    } else if (disposedRule == DisposedRule.ALL) {
                        if (buffEffects.stream().allMatch(buffEffect -> buffEffect.getBuffStatus() == BuffStatus.DISPOSED)) {
                            canDisposed = true;
                        }
                    }

                    if (canDisposed) {
                        switchBuffStatus(BuffStatus.DISPOSED, buffEffectCanDisposedEvent.getRemoveReason());
                    }
                }
            }
            default -> {

            }
        }
    }

    @Override
    public int getLife() {
        int maxLife = 0;
        for (IBuffEffect buffEffect : buffEffects) {
            if (buffEffect.getBuffStatus() == BuffStatus.ACTIVE) {
                maxLife = Math.max(maxLife, buffEffect.getLife());
            }
        }
        return maxLife;
    }

    @Override
    public int reduceLife(int life) {
        return changeLife(life, false);
    }

    @Override
    public int increaseLife(int life) {
        return changeLife(life, true);
    }

    private int changeLife(int life, boolean isIncrease) {
        FightEventBus fightEventBus = fightContext.getFightEventBus();
        if (life > 0) {
            for (IBuffEffect buffEffect : buffEffects) {
                if (buffEffect.getBuffStatus() == BuffStatus.ACTIVE) {
                    if (isIncrease) {
                        buffEffect.increaseLife(life);
                    } else {
                        buffEffect.reduceLife(life);
                    }
                }
            }

            fightEventBus.postEvent(new BuffUpdateEvent(this));
        }
        return getLife();
    }

    @Override
    public void onCreate() {

        subscribeEvent(FightEventEnum.SMALL_ROUND_END);
        subscribeEvent(FightEventEnum.BUFF_EFFECT_CAN_ACTIVE);
        subscribeEvent(FightEventEnum.BUFF_EFFECT_CAN_DISPOSED);

        TemplateBuff templateBuff = TemplateManager.inst().helper(BuffHelper.class).getTemplate(buffId);
        if (templateBuff == null) {
            throw new BattleException("buff不存在, id=" + buffId);
        }
       List<Integer> effect = templateBuff.effect;
       List<String> params = templateBuff.params;
       List<String> activeConditions = templateBuff.activeCondition;
       List<String> disposedConditions = templateBuff.disposedCondition;

        for (int i = 0; i < effect.size(); i++) {
            int effectId = effect.get(i);
            String param = params.get(i);
            String activeCondition = activeConditions.get(i);
            String disposedCondition = disposedConditions.get(i);
            IBuffEffect buffEffect = BuffFactory.createBuffEffect(this, effectId, param, activeCondition, disposedCondition);
            this.buffEffects.add(buffEffect);
        }

        this.buffEffects.forEach(IBuffEffect::onCreate);

        //最后注册监听
        this.getFightContext().getFightEventBus().register(this);
    }

    @Override
    public void onDestroy(RemoveReason reason) {
        this.buffEffects.forEach(e -> e.onDestroy(reason));
        this.getFightContext().getFightEventBus().unregister(this);
    }

    private void switchBuffStatus(BuffStatus nextStatus, RemoveReason removeReason) {

        BuffStatus prevStatus = buffStatus;
        buffStatus = nextStatus;

        Log.battle.info("switchBuffStatus, pre={}, now={}, removeReason={}", prevStatus, nextStatus, removeReason);

        switch (nextStatus) {
            case ACTIVE:
                // 盒子激活
                fightContext.getFightEventBus().postEvent(new BuffSwitchToActiveEvent(this));
                break;
            case DISPOSED:
                fightContext.getFightMatch().getFightHero(this.ownerId).getBuffManager().removeBuff(this.id, removeReason);
                this.onDestroy(removeReason);
                break;
        }
    }

    @Override
    public BuffBoxPb pack() {
        BuffBoxPb.Builder builder = BuffBoxPb.newBuilder();
        builder.setId(id);
        builder.setBuffId(buffId);
        builder.setOwnerId(ownerId);
        builder.setBuffStatus(buffStatus);
        return builder.build();
    }
}
