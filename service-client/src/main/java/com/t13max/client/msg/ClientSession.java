package com.t13max.client.msg;

import com.google.protobuf.MessageLite;
import com.t13max.game.exception.CommonException;
import com.t13max.game.msg.ClientMessagePack;
import com.t13max.game.msg.MessageManager;
import com.t13max.game.session.BaseSession;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: t13max
 * @since: 17:43 2024/5/30
 */
public class ClientSession extends BaseSession {

    public ClientSession(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public void close() {
        ctx.channel().close();
    }

    @Override
    public void sendMessage(int msgId, int resCode, MessageLite messageLite) {
        throw new CommonException("非法才操作, ClientSession.sendMessage, resCode!!!");
    }

    @Override
    public void sendMessage(int msgId, MessageLite messageLite) {
        MessageManager.inst().sendMessage(this, new ClientMessagePack<>(msgId, messageLite));
    }

    @Override
    public void sendError(int msgId, int errorCode) {
        throw new CommonException("非法才操作, ClientSession.sendError");
    }
}
