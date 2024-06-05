package com.t13max.client.msg;

import battle.api.FightMatchActionUnitPush;
import battle.api.JoinFightMatchResp;
import battle.entity.FightMatchPb;
import com.t13max.client.entity.MatchEntity;
import com.t13max.client.player.Player;
import com.t13max.game.msg.Message;
import message.id.MessageId;

/**
 * @author: t13max
 * @since: 17:13 2024/5/30
 */
@Message(value = MessageId.S_ACTION_UNIT_PUSH_VALUE)
public class ActionUnitPushMessage extends AbstractMessage<FightMatchActionUnitPush> {

    @Override
    public void doMessage(Player player, int msgId, FightMatchActionUnitPush message) {
        long playerId = message.getPlayerId();
        long heroId = message.getHeroId();
        if (playerId == player.getUuid()) {
            player.actionHero(heroId);
        }
    }
}
