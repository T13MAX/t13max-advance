package com.t13max.scene;

import com.t13max.game.config.SceneConfig;
import com.t13max.game.run.Application;
import com.t13max.game.run.ServerConfig;
import com.t13max.util.Log;

import java.util.concurrent.locks.LockSupport;

/**
 * @author: t13max
 * @since: 13:58 2024/5/23
 */
@ServerConfig(configClazz = SceneConfig.class)
public class SceneApplication {

    public static void main(String[] args) {

        Application.run(SceneApplication.class, args);

        Log.scene.info("SceneApplication run!");

        LockSupport.park();
    }

}
