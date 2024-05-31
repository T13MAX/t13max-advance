package com.t13max.client.msg;

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
@Message(value = MessageId.S_JOIN_MATCH_VALUE)
public class JoinMatchRespMessage extends AbstractMessage<JoinFightMatchResp> {


    @Override
    public void doMessage(Player player, int msgId, JoinFightMatchResp message) {
        FightMatchPb fightMatchPb = message.getFightMatchPb();

        MatchEntity matchEntity = new MatchEntity(fightMatchPb);
        player.setMatchEntity(matchEntity);
    }
}
