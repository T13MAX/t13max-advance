package com.t13max.client.msg;

import battle.api.FightMatchUpdatePush;
import battle.entity.FightMatchPb;
import com.t13max.client.player.Player;
import com.t13max.common.msg.Message;
import message.id.MessageId;

/**
 * @author: t13max
 * @since: 17:13 2024/5/30
 */
@Message(value = MessageId.S_MATCH_UPDATE_PUSH_VALUE)
public class MatchUpdatePushMessage extends AbstractMessage<FightMatchUpdatePush> {

    @Override
    public void doMessage(Player player, int msgId, FightMatchUpdatePush message) {
        FightMatchPb fightMatchPb = message.getFightMatchPb();
        player.getMatchEntity().update(fightMatchPb);
    }
}
