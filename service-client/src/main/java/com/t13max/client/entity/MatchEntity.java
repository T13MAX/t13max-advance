package com.t13max.client.entity;

import battle.entity.FightMatchPb;
import battle.entity.FightPlayerInfoPb;
import com.t13max.client.player.Player;
import lombok.Getter;
import lombok.Setter;


/**
 * @author: t13max
 * @since: 19:20 2024/5/29
 */
@Getter
@Setter
public class MatchEntity implements IEntity {

    private long matchId;

    private PlayerEntity selfPlayer;

    private PlayerEntity targetPlayer;

    public MatchEntity(FightMatchPb fightMatchPb) {
        matchId = fightMatchPb.getMatchId();
        for (FightPlayerInfoPb fightPlayerInfoPb : fightMatchPb.getPlayerDataList()) {
            if (fightPlayerInfoPb.getPlayerId() == Player.PLAYER.getUuid()) {
                selfPlayer = new PlayerEntity(fightPlayerInfoPb);
            } else {
                targetPlayer = new PlayerEntity(fightPlayerInfoPb);
            }
        }

        onChange();
    }


    @Override
    public void onChange() {
        selfPlayer.onChange();
        targetPlayer.onChange();
    }
}
