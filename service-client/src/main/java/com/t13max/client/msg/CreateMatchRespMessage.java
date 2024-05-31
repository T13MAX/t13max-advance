package com.t13max.client.msg;

import battle.api.CreateFightMatchResp;
import battle.api.JoinFightMatchReq;
import com.t13max.client.player.Player;
import com.t13max.client.player.task.SendMsgTask;
import com.t13max.game.msg.Message;
import message.id.MessageId;

/**
 * @author: t13max
 * @since: 17:13 2024/5/30
 */
@Message(value = MessageId.S_CREATE_MATCH_VALUE)
public class CreateMatchRespMessage extends AbstractMessage<CreateFightMatchResp> {

    private void error(int errorCode) {

    }

    private void success(Player player, CreateFightMatchResp createFightMatchResp) {
        //成功后尝试加入
        JoinFightMatchReq.Builder builder = JoinFightMatchReq.newBuilder();
        builder.setMatchId(player.getMatchId());
        new SendMsgTask(MessageId.C_JOIN_MATCH_VALUE, builder.build()).submit();
    }

    @Override
    public void doMessage(Player player, int msgId, CreateFightMatchResp message) {
        success(player, message);
    }
}
