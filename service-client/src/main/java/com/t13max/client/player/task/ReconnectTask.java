package com.t13max.client.player.task;

import com.t13max.client.client.NettyClient;
import com.t13max.client.msg.ClientSession;
import com.t13max.util.Log;
import io.netty.channel.ChannelFuture;

/**
 * @author: t13max
 * @since: 11:46 2024/6/6
 */
public class ReconnectTask extends AutoRetryTask {


    @Override
    public void run() {
        ClientSession clientSession = player.getClientSession();
        if (clientSession.getChannel().isActive()) {
            return;
        }

        NettyClient nettyClient = player.getNettyClient();
        try {
          nettyClient.connect();
        } catch (InterruptedException e) {
            Log.client.error("连接失败, 重试! error={}", e.getMessage());
        }
        clientSession = player.getClientSession();
        if (!clientSession.getChannel().isActive()) {
            retry();
        }
    }

}
