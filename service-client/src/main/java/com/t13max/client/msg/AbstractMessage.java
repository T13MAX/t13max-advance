package com.t13max.client.msg;

import com.google.protobuf.MessageLite;
import com.t13max.client.player.Player;
import com.t13max.game.msg.IMessage;
import com.t13max.game.msg.MessagePack;
import com.t13max.game.msg.ServerMessagePack;
import com.t13max.game.session.ISession;
import com.t13max.util.Log;

/**
 * @author: t13max
 * @since: 17:32 2024/5/30
 */
public abstract class AbstractMessage<T extends MessageLite> implements IMessage<T> {

    @Override
    public final void doMessage(ISession session, MessagePack<T> messagePack) {

        Player player = Player.PLAYER;

        try {
            if (!(messagePack instanceof ServerMessagePack<T> serverMessagePack)) {
                Log.client.error("处理消息错误 打包方式不对");
                return;
            }
            int msgId = serverMessagePack.getMsgId();
            int resCode = serverMessagePack.getResCode();
            if (resCode != 0) {
                doErrorMessage(player, msgId, resCode);
                return;
            }
            T messageLite = serverMessagePack.getMessageLite();
            doMessage(player, msgId, messageLite);
        } catch (Exception e) {
            //消息异常处理
            Log.client.error("消息处理异常, error={}", e.getMessage());
        }

    }

    public abstract void doMessage(Player player, int msgId, T message);

    public void doErrorMessage(Player player, int msgId, int resCode) {

    }
}
