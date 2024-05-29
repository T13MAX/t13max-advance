package com.t13max.client.frame;

import com.t13max.client.consts.Const;

import javax.swing.*;
import java.awt.*;

/**
 * @author: t13max
 * @since: 15:36 2024/5/29
 */
public class ClientMainFrame {

    public void open() {
        JFrame mainFrame = new JFrame(Const.MAIN_FRAME_TITLE);
        mainFrame.setSize(400, 300);//设置窗口大小，setSize(x, y)
        mainFrame.setLocationRelativeTo(null);//设置窗口默认居中显示

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        mainFrame.add(createSettingsJPanel(), BorderLayout.NORTH);
        mainFrame.add(createGameJPanel(), BorderLayout.CENTER);
        mainFrame.add(createLogJPanel(), BorderLayout.EAST);
        mainFrame.add(createOperateJPanel(), BorderLayout.SOUTH);

        mainFrame.setVisible(true);//最后设置窗口可见
    }

    private Component createLogJPanel() {
        JScrollPane logJPanel = new JScrollPane();
        logJPanel.setBackground(Color.WHITE);
        return logJPanel;
    }

    private Component createSettingsJPanel() {

        JPanel settingsJPanel = new JPanel();
        settingsJPanel.setBackground(Color.GRAY);
        settingsJPanel.add(new JButton("settings"));
        return settingsJPanel;
    }

    private Component createOperateJPanel() {
        JPanel operateJPanel = new JPanel();
        operateJPanel.setBackground(Color.green);
        return operateJPanel;
    }

    private Component createGameJPanel() {
        JPanel gameJPanel = new JPanel();
        gameJPanel.setLayout(new BorderLayout());
        gameJPanel.setBackground(Color.lightGray);

        JPanel targetJPanel = new JPanel();
        targetJPanel.setBackground(Color.red);

        JPanel selfJPanel = new JPanel();
        selfJPanel.setBackground(Color.blue);
        gameJPanel.add(targetJPanel, BorderLayout.NORTH);
        gameJPanel.add(selfJPanel, BorderLayout.CENTER);
        return gameJPanel;
    }
}
