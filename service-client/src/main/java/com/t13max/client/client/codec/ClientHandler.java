package com.t13max.client.client.codec;

import com.t13max.client.player.Player;
import com.t13max.game.session.ISession;
import com.t13max.game.session.SessionManager;
import com.t13max.util.Log;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;

/**
 * @author: t13max
 * @since: 19:44 2024/5/29
 */
public class ClientHandler extends ChannelDuplexHandler {

    private static final byte[] EMPTY_BYTES = new byte[0];

    public ClientHandler() {

    }

    public void channelActive(ChannelHandlerContext ctx) {
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        Log.common.info("{} active!!!", socketAddress);
    }

    // 断开连接
    public void channelInactive(ChannelHandlerContext ctx) {
        Log.common.info("{} inactive!!!");
    }

    // 收到消息
    public void channelRead(final ChannelHandlerContext ctx, Object obj) throws Exception {

        ByteBuf buf = (ByteBuf) obj;

        try {
            int msgId = buf.readInt();
            int resCode = buf.readInt();
            byte[] data = EMPTY_BYTES;
            if (buf.readableBytes() > 0) {
                data = new byte[buf.readableBytes()];
                buf.readBytes(data);
            }

            Player.PLAYER.receiveMessage(msgId, resCode, data);

        } finally {
            buf.release();
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
        Log.common.error("close game session because of error: {}", cause);
    }
}

