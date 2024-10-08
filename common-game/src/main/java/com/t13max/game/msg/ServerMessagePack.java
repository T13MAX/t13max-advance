package com.t13max.game.msg;

import com.google.protobuf.MessageLite;
import com.t13max.common.msg.MessagePack;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import lombok.Data;

/**
 * @author: t13max
 * @since: 18:14 2024/5/30
 */
@Data
public class ServerMessagePack<T extends MessageLite> extends MessagePack<T> {

    private int resCode;

    public ServerMessagePack(int msgId, int resCode, T messageLite) {
        this.msgId = msgId;
        this.resCode = resCode;
        this.messageLite = messageLite;
    }

    public ServerMessagePack(int msgId, int resCode) {
        this.msgId = msgId;
        this.resCode = resCode;
    }

    public ByteBuf wrapBuffers() {
        int len = MessageConst.CLIENT_HEADER_LENGTH;
        byte[] data = null;
        if (messageLite != null) {
            data = messageLite.toByteArray();
        }
        if (null != data) len += data.length;
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer(len);
        buf.writeInt(len);
        buf.writeInt(msgId);
        buf.writeInt(resCode);
        if (null != data) {
            buf.writeBytes(data);
        }
        return buf;
    }
}
