package com.t13max.fight.member;

import battle.entity.FightPlayerInfoPb;
import com.t13max.fight.context.FightContext;
import com.t13max.fight.hero.FightHero;
import lombok.Data;

/**
 * @author: t13max
 * @since: 14:29 2024/4/16
 */
@Data
public abstract class FightBaseMember implements IFightMember {

    protected long uid;

    protected boolean attacker;

    protected transient FightContext fightContext;

    @Override
    public long getId() {
        return uid;
    }

    @Override
    public FightPlayerInfoPb buildFightPlayerInfoPb() {
        FightPlayerInfoPb.Builder builder = FightPlayerInfoPb.newBuilder();
        builder.setPlayerId(uid);

        if (this instanceof MonsterMember) {
            builder.setMonsterGroupId((int) uid);
        }

        for (FightHero fightHero : fightContext.getFightMatch().getHeroMap().values()) {
            if (fightHero.getFightMember() != this) {
                continue;
            }
            builder.addHeroList(fightHero.buildFightHeroInfoPb());
        }
        return builder.build();
    }
}
