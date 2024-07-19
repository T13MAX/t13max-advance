package com.t13max.client.client.codec;

import com.google.protobuf.MessageLite;
import com.t13max.client.msg.ClientSession;
import com.t13max.client.player.Player;
import com.t13max.client.player.task.AutoRetryTask;
import com.t13max.client.player.task.ReconnectTask;
import com.t13max.game.msg.MessageManager;
import com.t13max.game.msg.ServerMessagePack;
import com.t13max.game.util.Log;
import io.netty.buffer.ByteBuf;
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
        Log.msg.info("{} active!!!", socketAddress);
        ClientSession clientSession = new ClientSession(ctx);
        Player.PLAYER.setClientSession(clientSession);
    }

    // 断开连接
    public void channelInactive(ChannelHandlerContext ctx) {
        Log.msg.info("{} inactive!!!");
        new ReconnectTask().submit();
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

            byte[] finalData = data;
            Player.PLAYER.addTask(new AutoRetryTask() {
                @Override
                public void run() {
                    MessageLite messageLite = MessageManager.inst().parseMessage(msgId, finalData);
                    MessageManager.inst().doMessage(Player.PLAYER.getClientSession(), new ServerMessagePack(msgId, resCode, messageLite));
                }
            });

        } finally {
            buf.release();
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
        Log.msg.error("close game session because of error: {}", cause);
    }
}

