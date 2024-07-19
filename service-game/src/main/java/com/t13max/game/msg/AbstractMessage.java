package com.t13max.game.msg;

import com.google.protobuf.MessageLite;
import com.t13max.common.action.IJobName;
import com.t13max.game.player.Player;
import com.t13max.game.player.PlayerManager;
import com.t13max.game.run.Application;
import com.t13max.game.session.ISession;
import com.t13max.game.util.Log;

/**
 * @author: t13max
 * @since: 14:40 2024/5/23
 */
public abstract class AbstractMessage<T extends MessageLite> implements IMessage<T> {

    @Override
    public final void doMessage(ISession session, MessagePack<T> messagePack) {

        try {
            int msgId = messagePack.getMsgId();
            T message = messagePack.getMessageLite();
            Player player = PlayerManager.inst().getPlayer(session.getUuid());
            if (player == null) {
                Application.autoLogger().error("玩家不存在, 无法处理消息, uuid={}, message={}", session.getUuid(), message);
                return;
            }
            //在自己的业务线程池中执行
            player.execute(IJobName.DEF, () -> doMessage(player, message));
            Log.msg.info("receiveMessage, uuid={}, msgId={}, message={}", session.getUuid(), msgId, message, getClass().getSimpleName());
        }catch (Exception e){
            //消息异常处理
            Log.msg.error("消息处理异常, error={}", e.getMessage());
        }

    }

    public abstract void doMessage(Player player, T message);

}
