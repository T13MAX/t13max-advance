package com.t13max.game.session;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户端到网关的Session
 *
 * @author: t13max
 * @since: 20:12 2024/5/28
 */
@Getter
@Setter
public class Client2GateSession extends BaseSession {

    private String token;

    public Client2GateSession(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

}
