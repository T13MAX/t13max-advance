package com.t13max.game.server.codec;

import com.t13max.common.session.ISession;
import com.t13max.common.session.SessionManager;
import com.t13max.game.session.Client2GateSession;
import com.t13max.game.util.Log;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 客户端到网关的Handler
 *
 * @Author t13max
 * @Date 16:15 2024/11/5
 */
@ChannelHandler.Sharable
public class Client2GateHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final byte[] EMPTY_BYTES = new byte[0];

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {

        Channel channel = ctx.channel();
        final ISession session = SessionManager.inst().getSession(channel);

        if (session == null) {
            Log.msg.error("session为空, 无法接收消息 ");
            return;
        }

        try {
            int msgId = buf.readInt();
            byte[] data;
            if (buf.readableBytes() > 0) {
                data = new byte[buf.readableBytes()];
                buf.readBytes(data);
            }

            //改为其他线程执行
            //MessageLite messageLite = MessageManager.inst().parseMessage(msgId, data);
            //if (messageLite == null) return;
            //MessageManager.inst().doMessage(session, new ClientMessagePack<>(msgId, messageLite));

        } finally {
            buf.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.msg.info("exceptionCaught:{}", cause.getMessage());
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        Log.msg.debug("Client:{} Added ", incoming);
        SessionManager.inst().putSession(new Client2GateSession(ctx));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        Log.msg.info("Client:{} Removed", incoming);
        SessionManager.inst().removeSession(incoming);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        Log.msg.info("userEventTriggered ctx {} event {}", ctx, evt);
        if (evt instanceof IdleStateEvent stateEvent) {
            if (stateEvent.state() == IdleState.WRITER_IDLE || stateEvent.state() == IdleState.ALL_IDLE) {
                ctx.flush();
            } else {
                //N秒后没有收到客户端的消息，自动断开
                Log.msg.info("heartBeat timeout {}", ctx.channel());
                ctx.close();
                Log.msg.info("server close {}", ctx.channel());
            }
        } else {
            Log.msg.info("client close {}", ctx.channel());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Log.msg.info("Client:{} Active", ctx);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.msg.info("Client:{} Inactive", ctx);
        super.channelInactive(ctx);
    }
}
