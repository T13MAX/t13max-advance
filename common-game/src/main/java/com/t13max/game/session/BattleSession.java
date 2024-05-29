package com.t13max.game.session;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author: t13max
 * @since: 20:12 2024/5/28
 */
public class BattleSession extends BaseSession {

    public BattleSession(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }


}
