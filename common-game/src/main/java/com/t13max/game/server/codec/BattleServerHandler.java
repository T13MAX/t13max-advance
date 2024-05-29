package com.t13max.game.server.codec;

import com.t13max.game.event.GameEventBus;
import com.t13max.game.event.SessionCLoseEvent;
import com.t13max.game.msg.MessageManager;
import com.t13max.game.session.SessionManager;
import com.t13max.game.session.BattleSession;
import com.t13max.game.session.ISession;
import com.t13max.util.Log;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;

/**
 * @author: t13max
 * @since: 20:10 2024/5/28
 */
public class BattleServerHandler extends ChannelDuplexHandler {

    private static final byte[] EMPTY_BYTES = new byte[0];

    public BattleServerHandler() {

    }

    public void channelActive(ChannelHandlerContext ctx) {
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        Log.common.info("{} active!!!", socketAddress);
        ISession session = new BattleSession(ctx);
        SessionManager.inst().putSession(session);
    }

    // 断开连接
    public void channelInactive(ChannelHandlerContext ctx) {
        final ISession session = SessionManager.inst().removeSession(ctx.channel());
        if (session == null) return;
        GameEventBus.inst().postEvent(new SessionCLoseEvent(session.getUuid()));
        Log.common.info("{} inactive!!!", session);
    }

    // 收到消息
    public void channelRead(final ChannelHandlerContext ctx, Object obj) throws Exception {

        ByteBuf buf = (ByteBuf) obj;
        Channel channel = ctx.channel();
        final ISession session = SessionManager.inst().getSession(channel);

        if (session == null) {
            Log.common.error("session为空, 无法接收消息 ");
            return;
        }

        try {
            int msgId = buf.readInt();
            byte[] data = EMPTY_BYTES;
            if (buf.readableBytes() > 0) {
                data = new byte[buf.readableBytes()];
                buf.readBytes(data);
            }

            MessageManager.inst().doMessage(session, msgId, data);

        } finally {
            buf.release();
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 异常
        final ISession session = SessionManager.inst().getSession(ctx.channel());
        ctx.channel().close();
        Log.common.error("close game session because of error: {} {}", session, cause);
    }
}
