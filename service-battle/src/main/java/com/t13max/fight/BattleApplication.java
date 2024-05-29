package com.t13max.fight;

import com.t13max.game.config.BattleConfig;
import com.t13max.game.run.Application;
import com.t13max.game.run.ServerConfig;
import com.t13max.util.Log;

import java.util.concurrent.locks.LockSupport;

/**
 * @author: t13max
 * @since: 14:01 2024/5/23
 */
@ServerConfig(configClazz = BattleConfig.class)
public class BattleApplication {

    public static void main(String[] args) throws Exception {

        Application.run(BattleApplication.class, args);

        Log.scene.info("BattleApplication run!");

        LockSupport.park();
    }
}
