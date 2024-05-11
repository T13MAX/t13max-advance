package com.t13max.fight.buff;

import com.t13max.fight.FightHero;
import com.t13max.fight.buff.condition.IEventCondition;
import com.t13max.fight.buff.effect.IBuffEffect;
import com.t13max.fight.event.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: t13max
 * @since: 15:57 2024/4/10
 */
@Data
public class BuffBoxImpl extends AbstractEventListener implements IBuffBox {

    private long id;

    private FightHero owner;

    private Set<IBuffEffect> buffEffects;

    private int life;

    protected transient FightEventBus fightEventBus;

    @Override
    public void onEvent(IFightEvent event) {
        switch (event.getFightEventEnum()) {
            case SMALL_ROUND_END -> {
                this.reduceLife(1);
                if (checkLife()) {
                    this.onDestroy(RemoveReason.DISPOSED);
                }
            }
            default -> {

            }
        }
    }

    private boolean checkLife() {
        return life > 0;
    }

    @Override
    public int getLife() {
        return life;
    }

    @Override
    public int reduceLife(int reduce) {
        return life -= reduce;
    }

    @Override
    public int increaseLife(int increase) {
        return life += increase;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy(RemoveReason reason) {
        this.owner.getBuffManager().removeBuff(this.id);
        this.buffEffects.forEach(e -> e.onDestroy(reason));
    }


}
