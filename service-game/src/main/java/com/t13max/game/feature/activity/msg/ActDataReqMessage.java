package com.t13max.game.feature.activity.msg;

import com.t13max.common.msg.Message;
import com.t13max.game.feature.activity.ActivityMemory;
import com.t13max.game.msg.AbstractMessage;
import com.t13max.game.player.Player;
import game.entity.ActivityDataListPb;
import game.entity.ActivityDataReq;
import message.id.MessageId;

/**
 * @author: t13max
 * @since: 13:50 2024/6/7
 */
@Message(MessageId.C_GET_ACT_DATA_VALUE)
public class ActDataReqMessage extends AbstractMessage<ActivityDataReq> {

    @Override
    public void doMessage(Player player, ActivityDataReq message) {
        ActivityDataListPb activityDataListPb = player.getMemory(ActivityMemory.class).buildPb();
        player.sendMsg(MessageId.S_GET_ACT_DATA_VALUE,activityDataListPb);
    }
}
