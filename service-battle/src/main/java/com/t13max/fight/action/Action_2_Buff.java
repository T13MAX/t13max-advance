package com.t13max.fight.action;

import com.t13max.fight.hero.FightHero;
import com.t13max.fight.FightMatch;
import com.t13max.fight.buff.BuffBoxImpl;
import com.t13max.fight.buff.BuffFactory;
import com.t13max.fight.event.FightEventBus;
import lombok.extern.log4j.Log4j2;

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
        FightMatch fightMatch = fightContext.getFightMatch();
        FightEventBus fightEventBus = fightContext.getFightEventBus();
        for (Long targetHeroId : this.getTargetHeroIds()) {
            FightHero fightHero = fightMatch.getFightHero(targetHeroId);
            if (fightHero == null) {
                continue;
            }

            BuffBoxImpl buffBoxImpl = BuffFactory.createBuffBoxImpl(fightContext, fightHero.getId(), this.param);
            if (buffBoxImpl == null) return;
            fightHero.getBuffManager().addBuff(buffBoxImpl);
            fightEventBus.register(buffBoxImpl);
        }
    }


}
