package com.t13max.game.server;

import com.t13max.common.net.AbstractServer;
import com.t13max.game.server.codec.BattleServerHandler;
import com.t13max.game.server.codec.FrameDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * battle服的监听
 *
 * @author: t13max
 * @since: 18:44 2024/5/23
 */
public class BattleServer extends AbstractServer {

    @Override
    protected void initInitializer() {

        this.channelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new IdleStateHandler(300, 300, 300));
                pipeline.addLast("FrameDecoder", new FrameDecoder());
                pipeline.addLast("BattleServerHandler", new BattleServerHandler());
            }
        };
    }
}
