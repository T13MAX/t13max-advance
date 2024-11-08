package com.t13max.client;

import com.t13max.client.player.Player;
import com.t13max.common.run.Application;
import com.t13max.common.run.ConfigClazz;
import com.t13max.game.config.ClientConfig;
import com.t13max.game.util.Log;

/**
 * @author: t13max
 * @since: 17:19 2024/5/28
 */
@ConfigClazz(configClazz = ClientConfig.class)
public class ClientApplication {

    public static void main(String[] args) throws Exception {

        Application.run(ClientApplication.class, args);

        try {
            //FlatLightLaf.install();
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.put( "Button.arc", 0 );
            //UIManager.put( "Component.arrowType", "chevron" );
        } catch (Exception e) {
            Log.client.error("启动失败, error={}", e.getMessage());
        }
        Player.PLAYER.openWindow("login");
        Log.client.info("client run !!!");

    }
}
