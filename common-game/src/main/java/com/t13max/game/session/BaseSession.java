package com.t13max.game.session;

import com.google.protobuf.MessageLite;
import com.t13max.game.msg.MessageManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: t13max
 * @since: 20:56 2024/5/28
 */
@Getter
@Setter
public abstract class BaseSession implements ISession {
    //唯一id
    protected long uuid;
    //角色id 暂时涉及不到 为了适配未来一个玩家多个角色预留的字段
    protected long roleId;

    protected ChannelHandlerContext ctx;

    @Override
    public Channel getChannel() {
        return this.ctx.channel();
    }

    @Override
    public void sendMessage(int msgId, int resCode, MessageLite messageLite) {
        MessageManager.inst().sendMessage(this, msgId, resCode, messageLite);
    }

    @Override
    public void sendMessage(int msgId, MessageLite messageLite) {
        MessageManager.inst().sendMessage(this, msgId, 0, messageLite);
    }

    @Override
    public void sendError(int msgId, int errorCode) {
        MessageManager.inst().sendError(this, msgId, errorCode);
    }
}
