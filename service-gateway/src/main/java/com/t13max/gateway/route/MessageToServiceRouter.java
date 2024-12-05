package com.t13max.gateway.route;

import com.t13max.common.manager.ManagerBase;
import com.t13max.common.run.Application;
import com.t13max.common.session.ISession;
import com.t13max.gateway.util.Log;
import com.t13max.util.limiter.CounterRateLimiter;
import com.t13max.util.limiter.IRateLimiter;

/**
 * 客户端消息从网关到game服的路由
 *
 * @Author t13max
 * @Date 15:54 2024/11/5
 */
public class MessageToServiceRouter extends ManagerBase {

    private final IRateLimiter rateLimiter = new CounterRateLimiter(1500, 1000);

    private volatile boolean shutdown;

    @Override
    protected void init() {

        Log.GATE.info("MessageToServiceRouter init {}", Application.config().getInstanceName());
    }

    @Override
    protected void onShutdown() {
        shutdown = true;
    }

    public void handleClientMessage(ISession session, int messageId, byte[] messageData) {

        if (shutdown) {
            return;
        }

        rateLimiter.tryAcquire();

        //todo atb 根据message确定要去的服务器
    }
}
