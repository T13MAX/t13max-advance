package com.t13max.fight;

import battle.api.CreateFightMatchReq;
import battle.api.LoginBattleReq;
import com.t13max.fight.msg.CreateMatchMessage;
import com.t13max.fight.msg.LoginBattleMessage;
import com.t13max.game.config.BattleConfig;
import com.t13max.game.msg.MessageManager;
import com.t13max.game.run.Application;
import com.t13max.game.run.NettyServer;
import com.t13max.game.run.ApplicationConfig;
import com.t13max.game.server.BattleServer;
import com.t13max.util.Log;
import message.id.MessageId;

import java.util.concurrent.locks.LockSupport;

/**
 * @author: t13max
 * @since: 14:01 2024/5/23
 */
@ApplicationConfig(configClazz = BattleConfig.class)
@NettyServer(serverClazz = BattleServer.class)
public class BattleApplication {

    public static void main(String[] args) throws Exception {

        Application.run(BattleApplication.class, args);

        Log.scene.info("BattleApplication run!");

        init();

        LockSupport.park();
    }

    /**
     * battle自己的初始化东东
     *
     * @Author t13max
     * @Date 19:49 2024/5/30
     */
    private static void init() {
        MessageManager.inst().addMessage(MessageId.C_BATTLE_LOGIN_VALUE, new LoginBattleMessage(), LoginBattleReq.class);
        MessageManager.inst().addMessage(MessageId.C_CREATE_MATCH_VALUE, new CreateMatchMessage(), CreateFightMatchReq.class);

    }
}
