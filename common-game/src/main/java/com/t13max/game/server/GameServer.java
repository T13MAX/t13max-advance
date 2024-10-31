package com.t13max.game.server;

import com.t13max.common.config.NettyConfig;
import com.t13max.common.net.AbstractServer;
import com.t13max.common.run.Application;
import com.t13max.game.util.Log;
import com.t13max.util.ThreadNameFactory;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

/**
 * game服的监听
 *
 * @author: t13max
 * @since: 18:44 2024/5/23
 */
public class GameServer extends AbstractServer {

    @Override
    protected void init() {
        NettyConfig nettyConfig = Application.config().getNetty();
        int threadNum = Runtime.getRuntime().availableProcessors() * 2;
        if (nettyConfig.isUseEpoll()) {
            this.bossGroup = new EpollEventLoopGroup(1, new ThreadNameFactory("epoll-boss"));
            this.workerGroup = new EpollEventLoopGroup(threadNum, new ThreadNameFactory("epoll-worker"));
            Log.msg.info("use EpollEventLoopGroup.....");
        } else {
            this.bossGroup = new NioEventLoopGroup(1, new ThreadNameFactory("nio-boss"));
            this.workerGroup = new NioEventLoopGroup(threadNum, new ThreadNameFactory("nio-worker"));
            Log.msg.info("use NioEventLoopGroup.....");
        }

        this.channelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
//				ch.pipeline().addLast(new IdleStateHandler(300, 300, 300));
//                pipeline.addLast("FrameDecoder", new FrameDecoder());
                //               pipeline.addLast("ServerHandler", new ServerHandler(gameContext));
            }
        };
    }
}
