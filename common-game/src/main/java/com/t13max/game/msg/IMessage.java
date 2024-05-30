package com.t13max.game.msg;

import com.google.protobuf.MessageLite;
import com.t13max.game.session.ISession;

/**
 * @author: t13max
 * @since: 19:41 2024/5/23
 */
public interface IMessage<T extends MessageLite> {

    void doMessage(ISession session, MessagePack<T> messagePack);
}
