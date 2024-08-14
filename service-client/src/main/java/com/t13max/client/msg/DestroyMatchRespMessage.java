package com.t13max.client.msg;

import battle.api.DestroyFightMatchResp;
import com.t13max.client.player.Player;
import com.t13max.common.msg.Message;
import message.id.MessageId;

/**
 * @author: t13max
 * @since: 17:13 2024/5/30
 */
@Message(value = MessageId.S_DESTROY_MATCH_VALUE)
public class DestroyMatchRespMessage extends AbstractMessage<DestroyFightMatchResp> {

    @Override
    public void doMessage(Player player, int msgId, DestroyFightMatchResp message) {
        Player.PLAYER.setMatchId(0);
    }
}
