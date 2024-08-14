package com.t13max.game.server.codec;

import com.google.protobuf.MessageLite;
import com.t13max.common.event.GameEventBus;
import com.t13max.common.msg.MessageManager;
import com.t13max.common.session.ISession;
import com.t13max.common.session.SessionManager;
import com.t13max.game.event.SessionCLoseEvent;
import com.t13max.game.msg.ClientMessagePack;
import com.t13max.game.session.BattleSession;
import com.t13max.game.util.Log;
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
        Log.msg.info("{} active!!!", socketAddress);
        ISession session = new BattleSession(ctx);
        SessionManager.inst().putSession(session);
    }

    // 断开连接
    public void channelInactive(ChannelHandlerContext ctx) {
        final ISession session = SessionManager.inst().removeSession(ctx.channel());
        if (session == null) return;
        GameEventBus.inst().postEvent(new SessionCLoseEvent(session.getUuid()));
        Log.msg.info("{} inactive!!!", session);
    }

    // 收到消息
    public void channelRead(final ChannelHandlerContext ctx, Object obj) throws Exception {

        ByteBuf buf = (ByteBuf) obj;
        Channel channel = ctx.channel();
        final ISession session = SessionManager.inst().getSession(channel);

        if (session == null) {
            Log.msg.error("session为空, 无法接收消息 ");
            return;
        }

        try {
            int msgId = buf.readInt();
            byte[] data = EMPTY_BYTES;
            if (buf.readableBytes() > 0) {
                data = new byte[buf.readableBytes()];
                buf.readBytes(data);
            }


            //改为其他线程执行
            MessageLite messageLite = MessageManager.inst().parseMessage(msgId, data);
            if (messageLite == null) return;

            MessageManager.inst().doMessage(session, new ClientMessagePack<>(msgId, messageLite));

        } finally {
            buf.release();
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 异常
        final ISession session = SessionManager.inst().getSession(ctx.channel());
        ctx.channel().close();
        Log.msg.error("close game session because of error: {} {}", session, cause);
    }
}
