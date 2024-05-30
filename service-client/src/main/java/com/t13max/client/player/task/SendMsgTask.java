package com.t13max.client.player.task;

import com.google.protobuf.MessageLite;
import com.t13max.game.msg.MessageConst;
import com.t13max.util.Log;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
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
        Channel channel = player.getChannel();
        if (!channel.isActive()) {
            Log.common.error("sendMessage failed, channel inactive, uuid={}, msgId={}, message={}", player.getUuid(), msgId, messageLite, getClass().getSimpleName());
            return;
        }
        ByteBuf byteBuf = wrapBuffers(msgId,  messageLite == null ? null : messageLite.toByteArray());
        channel.writeAndFlush(byteBuf);
        Log.common.info("sendMessage, uuid={}, msgId={}, message={}", player.getUuid(), msgId, messageLite, getClass().getSimpleName());

    }

    public ByteBuf wrapBuffers(int msgId, byte[] data) {
        int len = MessageConst.HEADER_LENGTH;
        if (null != data) len += data.length;
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer(len);
        buf.writeInt(len);
        buf.writeInt(msgId);
        if (null != data) {
            buf.writeBytes(data);
        }
        return buf;
    }
}
