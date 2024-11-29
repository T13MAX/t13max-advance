package com.t13max.game.server;

import com.t13max.common.net.AbstractServer;
import com.t13max.game.server.codec.BattleServerHandler;
import com.t13max.game.server.codec.Client2GateHandler;
import com.t13max.game.server.codec.FrameDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 网关服
 *
 * @author: t13max
 * @since: 18:44 2024/5/23
 */
public class GatewayServer extends AbstractServer {

    @Override
    protected void initInitializer() {
        this.channelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new IdleStateHandler(300, 300, 300));
                pipeline.addLast("FrameDecoder", new FrameDecoder());
                pipeline.addLast("Client2GateHandler", new Client2GateHandler());
            }
        };
    }
}
