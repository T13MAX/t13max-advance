package com.t13max.game.session;

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

    protected long uuid;

    protected long roleId;

    protected ChannelHandlerContext ctx;

}
