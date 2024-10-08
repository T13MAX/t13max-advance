package com.t13max.game.feature.activity.msg;

import com.t13max.common.msg.Message;
import com.t13max.game.feature.activity.ActivityManager;
import com.t13max.game.feature.activity.ActivityMemory;
import com.t13max.game.feature.activity.data.SignInActData;
import com.t13max.game.msg.AbstractMessage;
import com.t13max.game.player.Player;
import com.t13max.game.util.Log;
import game.entity.ActSignInReq;
import game.entity.ActSignInResp;
import message.id.MessageId;

/**
 * @author: t13max
 * @since: 13:50 2024/6/7
 */
@Message(MessageId.C_ACT_SIGN_IN_VALUE)
public class ActSignInMessage extends AbstractMessage<ActSignInReq> {

    @Override
    public void doMessage(Player player, ActSignInReq message) {
        int index = message.getIndex();
        int actId = message.getActId();
        if (!ActivityManager.inst().checkActOpen(actId)) {
            Log.game.error("活动未开启, actId={}", actId);
            return;
        }

        //index的校验
        //略

        SignInActData signInActData = player.getMemory(ActivityMemory.class).get().getData(actId);
        signInActData.getSignInSet().add(index);
        ActSignInResp.Builder builder = ActSignInResp.newBuilder();
        builder.setIndex(index);
        builder.setActId(actId);
        player.sendMsg(MessageId.S_ACT_SIGN_IN_VALUE, builder.build());
    }
}
