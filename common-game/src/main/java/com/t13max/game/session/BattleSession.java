package com.t13max.game.session;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: t13max
 * @since: 20:12 2024/5/28
 */
@Getter
@Setter
public class BattleSession extends BaseSession {

    private long matchId;

    public BattleSession(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }


}
