package com.t13max.fight.log;

import com.t13max.fight.FightHero;
import com.t13max.fight.attr.FightAttrEnum;

/**
 * @author: t13max
 * @since: 18:43 2024/4/22
 */
public class UnitLog {

    private long id;

    private double curHp;

    private double maxHp;

    public UnitLog(FightHero fightHero) {
        this.id = fightHero.getId();
        this.curHp = fightHero.getFightAttrManager().getFinalAttr(FightAttrEnum.CUR_HP);
        this.maxHp = fightHero.getFightAttrManager().getFinalAttr(FightAttrEnum.MAX_HP);
    }

    @Override
    public String toString() {
        return "" + id + "hp:" + FightLogManager.DF.format(curHp) + "/" + FightLogManager.DF.format(maxHp);
    }
}
