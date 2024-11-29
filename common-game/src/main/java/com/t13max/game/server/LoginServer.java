package com.t13max.game.server;

import com.t13max.common.exception.CommonException;
import com.t13max.common.net.AbstractServer;
import com.t13max.game.server.codec.HttpServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.KeyManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * login服
 *
 * @author: t13max
 * @since: 18:44 2024/5/23
 */
public class LoginServer extends AbstractServer {

    private final static String PASSWORD = "password";
    private final static String PATH = "/keystore.jks";

    private final SslContext sslContext;

    public LoginServer() {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(PATH)) {
            if (inputStream != null) {
                KeyStore keyStore = KeyStore.getInstance("JKS");
                keyStore.load(inputStream, PASSWORD.toCharArray());
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, PASSWORD.toCharArray());
                sslContext = SslContextBuilder.forServer(keyManagerFactory).build();
            } else {
                // 使用自签名证书 (仅用于测试环境)
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslContext = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            }
        } catch (Exception exception) {
            throw new CommonException("SslContext创建失败");
        }
    }

    @Override
    protected void initInitializer() {
        this.channelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(sslContext.newHandler(ch.alloc()));
                pipeline.addLast(new HttpServerCodec());
                pipeline.addLast(new HttpObjectAggregator(65536));
                pipeline.addLast(new HttpServerExpectContinueHandler());
                pipeline.addLast(new HttpServerHandler());
            }
        };
    }
}
