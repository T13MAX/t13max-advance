package com.t13max.client.entity;

import battle.entity.FightHeroInfoPb;
import battle.entity.FightMatchPb;
import battle.entity.FightPlayerInfoPb;
import com.google.protobuf.MessageLite;
import com.t13max.client.player.Player;
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
            this.heroMap.put(fightHeroInfoPb.getHeroId(), new HeroEntity(fightHeroInfoPb, fightPlayerInfoPb.getPlayerId() == Player.PLAYER.getUuid(), i));
        }
    }

    @Override
    public void onChange() {
        heroMap.values().forEach(HeroEntity::onChange);
    }

    @Override
    public <T extends MessageLite> void update(T t) {

        if (!(t instanceof FightPlayerInfoPb playerInfoPb)) {
            return;
        }

        if (playerInfoPb.getMonsterGroupId() == 0) {
            playerId = playerInfoPb.getPlayerId();
        } else {
            playerId = playerInfoPb.getMonsterGroupId();
        }
        for (int i = 0; i < playerInfoPb.getHeroListList().size(); i++) {
            FightHeroInfoPb fightHeroInfoPb = playerInfoPb.getHeroList(i);
            HeroEntity heroEntity = this.heroMap.get(fightHeroInfoPb.getHeroId());
            if (heroEntity == null) {
                continue;
            }
            heroEntity.update(fightHeroInfoPb);
        }
    }
}
