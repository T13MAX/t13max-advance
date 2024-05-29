package com.t13max.game.session;

import com.google.protobuf.MessageLite;
import com.t13max.game.msg.MessageManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: t13max
 * @since: 20:12 2024/5/28
 */
public class BattleSession extends BaseSession{

    public BattleSession(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Channel getChannel(){
        return this.ctx.channel();
    }

    @Override
    public void sendMessage(MessageLite messageLite) {
        MessageManager.inst().sendMessage(this,messageLite);
    }
}
