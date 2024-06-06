package com.t13max.client.player.task;

import com.google.protobuf.MessageLite;
import com.t13max.client.player.IPlayerTask;
import com.t13max.game.msg.ClientMessagePack;
import com.t13max.game.msg.MessageManager;
import lombok.Data;

/**
 * @author: t13max
 * @since: 13:41 2024/5/30
 */
@Data
public class SendMsgTask extends AbstractTask {

    private int msgId;

    private MessageLite messageLite;

    public SendMsgTask(int msgId, MessageLite messageLite) {
        this.msgId = msgId;
        this.messageLite = messageLite;
    }

    @Override
    public void run() {
        MessageManager.inst().sendMessage(player.getClientSession(), new ClientMessagePack<>(msgId, messageLite));
    }


}
