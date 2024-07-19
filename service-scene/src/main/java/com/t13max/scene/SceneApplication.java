package com.t13max.scene;

import com.t13max.game.config.SceneConfig;
import com.t13max.game.run.Application;
import com.t13max.game.run.ApplicationConfig;
import com.t13max.game.util.Log;

import java.util.concurrent.locks.LockSupport;

/**
 * @author: t13max
 * @since: 13:58 2024/5/23
 */
@ApplicationConfig(configClazz = SceneConfig.class)
public class SceneApplication {

    public static void main(String[] args) throws Exception{

        Application.run(SceneApplication.class, args);

        Log.scene.info("SceneApplication run!");

        LockSupport.park();
    }

}
