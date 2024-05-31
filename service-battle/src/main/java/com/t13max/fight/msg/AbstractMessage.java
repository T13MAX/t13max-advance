package com.t13max.fight.msg;

import com.google.protobuf.MessageLite;
import com.t13max.game.msg.IMessage;
import com.t13max.game.msg.MessagePack;
import com.t13max.game.session.BattleSession;
import com.t13max.game.session.ISession;
import com.t13max.util.Log;

/**
 * @author: t13max
 * @since: 11:10 2024/5/24
 */
public abstract class AbstractMessage<T extends MessageLite> implements IMessage<T> {

    @Override
    public final void doMessage(ISession session, MessagePack<T> messagePack) {

        try {
            int msgId = messagePack.getMsgId();
            T message = messagePack.getMessageLite();
            if (!(session instanceof BattleSession battleSession)) {
                Log.client.error("消息处理异常, session类型错误={}", session.getClass().getSimpleName());
                return;
            }

            doMessage(battleSession, msgId, message);
        } catch (Exception e) {
            //消息异常处理
            Log.client.error("消息处理异常, error={}", e.getMessage());
        }

    }

    protected abstract void doMessage(BattleSession battleSession, int msgId, T message);
}
