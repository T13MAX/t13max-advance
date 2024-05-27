package com.t13max.game.msg;

import com.google.protobuf.MessageLite;

/**
 * @author: t13max
 * @since: 19:42 2024/5/23
 */
public interface ISession {

    long getUuid();

    long getRoleId();

    void sendMessage(MessageLite messageLite);
}
