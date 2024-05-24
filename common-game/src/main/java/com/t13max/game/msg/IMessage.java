package com.t13max.game.msg;

import com.google.protobuf.MessageLite;

/**
 * @author: t13max
 * @since: 19:41 2024/5/23
 */
public interface IMessage<T extends MessageLite> {

    void doMessage(ISession session, int msgId, T message);
}
