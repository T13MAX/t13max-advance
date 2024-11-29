package com.t13max.login;

import com.t13max.common.run.Application;
import com.t13max.common.run.ConfigClazz;
import com.t13max.common.run.ServerClazz;
import com.t13max.game.config.LoginConfig;
import com.t13max.game.server.LoginServer;

/**
 * @author: t13max
 * @since: 14:21 2024/5/29
 */
@ConfigClazz(configClazz = LoginConfig.class)
@ServerClazz(serverClazz = LoginServer.class)
public class LoginApplication {

    public static void main(String[] args) throws Exception {
        //启动一个web服务器
        Application.run(LoginApplication.class, args);
    }
}
