package com.t13max.client.view.settings.button;

import com.t13max.client.view.settings.LogFrame;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author: t13max
 * @since: 11:37 2024/5/30
 */
public class LogButton extends JButton {

    public static LogFrame logFrame = new LogFrame();

    public LogButton() {
        this.setText("日志");

        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (logFrame.isVisible()) {
                    logFrame.setVisible(false);
                } else {
                    logFrame.setVisible(true);
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
