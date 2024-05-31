package com.t13max.client.view.button;

import com.t13max.client.player.Player;
import com.t13max.client.view.window.AbstractWindow;
import com.t13max.client.view.window.LogWindow;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author: t13max
 * @since: 11:37 2024/5/30
 */
public class LogButton extends JButton {

    public LogButton() {
        this.setText("日志");

        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                LogWindow logWindow = Player.PLAYER.getWindow("log");
                if (logWindow == null) {
                    Player.PLAYER.openWindow("log");
                } else if (logWindow.isVisible()) {
                    logWindow.closeWindow();
                } else {
                    logWindow.openWindow();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
}
