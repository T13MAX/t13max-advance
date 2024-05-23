package com.t13max.game.server;

import com.t13max.game.config.BaseConfig;
import com.t13max.game.config.NettyConfig;
import com.t13max.game.run.Application;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author: t13max
 * @since: 18:42 2024/5/23
 */
public abstract class BaseServer {

    private ServerBootstrap bootstrap;

    private Channel channel;

    protected EventLoopGroup bossGroup;

    protected EventLoopGroup workerGroup;

    protected ChannelInitializer<SocketChannel> channelInitializer;

    public void startTcpServer() throws InterruptedException {

        init();

        BaseConfig config = Application.config();
        NettyConfig configNetty = config.getNetty();

        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(configNetty.isUseEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, configNetty.getConnectTimeoutMillis())
                // 在defaults.yaml文件中，low.watermark默认大小为8388608，即8M；high.watermark默认大小为16777216，即16M
                .option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(configNetty.getLowWaterMark(), configNetty.getHighWaterMark()))
                .option(ChannelOption.SO_BACKLOG, configNetty.getSoBackLog())
                .option(ChannelOption.SO_REUSEADDR, configNetty.isSsoReuseAddr())
                .childOption(ChannelOption.TCP_NODELAY, configNetty.isTcpNodelay())
                .childOption(ChannelOption.SO_KEEPALIVE, configNetty.isSsoKeepAlive())
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        bootstrap.childHandler(channelInitializer);

        InetSocketAddress address = new InetSocketAddress(configNetty.getPort());
        channel = bootstrap.bind(address).sync().channel();
        Application.autoLogger().info("netty server bind on {} success!", address);

    }

    /**
     * 关闭
     *
     * @Author t13max
     * @Date 19:12 2024/5/23
     */
    public void shutdown() {
        try {

            Application.autoLogger().info("netty shutdown begin!");

            if (null != channel) {
                ChannelFuture f = channel.close();
                f.awaitUninterruptibly();
            }

        } finally {
            if (this.workerGroup != null) {
                this.workerGroup.shutdownGracefully();
            }
            if (this.bossGroup != null) {
                this.bossGroup.shutdownGracefully();
            }
        }

        Application.autoLogger().info("NettyServer shutdown success!");
    }

    /**
     * 初始化各个组件
     *
     * @Author t13max
     * @Date 19:24 2024/5/23
     */
    protected abstract void init();

}
