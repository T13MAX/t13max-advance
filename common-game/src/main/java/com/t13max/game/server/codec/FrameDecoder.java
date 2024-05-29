package com.t13max.game.server.codec;

import com.t13max.game.msg.MessageConst;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 粘包拆包
 * 4字节的长度 4字节的msgId 数据
 *
 * @author: t13max
 * @since: 20:04 2024/5/28
 */
public class FrameDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        Object decoded = decode(channelHandlerContext, byteBuf);
        if (decoded != null) list.add(decoded);
    }

    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        if (in.isReadable(MessageConst.HEADER_LENGTH)) {

            // 标记读位置
            in.markReaderIndex();
            int frameLength = in.readInt();
            if (in.isReadable(frameLength))
                return in.readRetainedSlice(frameLength);
            else
                in.resetReaderIndex();

        }

        return null;
    }
}
