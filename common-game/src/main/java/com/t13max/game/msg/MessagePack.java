package com.t13max.game.msg;

import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * @author: t13max
 * @since: 18:12 2024/5/30
 */
@Data
public abstract class MessagePack<T extends MessageLite> {

    protected int msgId;

    protected T messageLite;

    public abstract ByteBuf wrapBuffers();
}
