package com.t13max.client.client;

import com.t13max.client.client.codec.ClientHandler;
import com.t13max.game.server.codec.FrameDecoder;
import com.t13max.util.ThreadNameFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author: t13max
 * @since: 19:44 2024/5/29
 */
public class NettyClient {

    private Bootstrap bootstrap;

    private EventLoopGroup workerGroup;

    public void start(){
        int threadNum = Runtime.getRuntime().availableProcessors() * 2;

    this.workerGroup = new NioEventLoopGroup(threadNum, new ThreadNameFactory("CLIENT-NIO-WORKER"));


        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);

        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new FrameDecoder());//拆包
                pipeline.addLast("ClientHandler", new ClientHandler());
            }
        });
    }
}
