package com.t13max.client.entity;

import battle.entity.FightHeroInfoPb;
import battle.entity.FightPlayerInfoPb;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 20:48 2024/5/30
 */
@Getter
@Setter
public class PlayerEntity implements IEntity {

    private long playerId;

    private Map<Long, HeroEntity> heroMap = new HashMap<>();

    public PlayerEntity(FightPlayerInfoPb fightPlayerInfoPb) {
        if (fightPlayerInfoPb.getMonsterGroupId() == 0) {
            playerId = fightPlayerInfoPb.getPlayerId();
        } else {
            playerId = fightPlayerInfoPb.getMonsterGroupId();
        }
        for (int i = 0; i < fightPlayerInfoPb.getHeroListList().size(); i++) {
            FightHeroInfoPb fightHeroInfoPb = fightPlayerInfoPb.getHeroList(i);
            this.heroMap.put(fightHeroInfoPb.getHeroId(), new HeroEntity(fightHeroInfoPb, i));
        }
    }

    @Override
    public void onChange() {

    }
}
