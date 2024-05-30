package com.t13max.client;

import com.t13max.client.view.login.LoginFrame;
import com.t13max.util.Log;

/**
 * @author: t13max
 * @since: 17:19 2024/5/28
 */
public class ClientApplication {

    public static void main(String[] args) {
        try {
            //FlatLightLaf.install();
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.put( "Button.arc", 0 );
            //UIManager.put( "Component.arrowType", "chevron" );
        } catch (Exception e) {
            Log.client.error("启动失败, error={}", e.getMessage());
        }
        LoginFrame.LOGIN_FRAME.setVisible(true);
    }
}
