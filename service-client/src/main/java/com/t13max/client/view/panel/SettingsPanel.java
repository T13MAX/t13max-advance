package com.t13max.client.view.panel;


import com.t13max.client.view.button.DestroyMatchButton;
import com.t13max.client.view.enums.Const;
import com.t13max.client.view.button.CreateMatchButton;
import com.t13max.client.view.button.LogButton;

import javax.swing.*;
import java.awt.*;

/**
 * @author: t13max
 * @since: 17:32 2024/5/29
 */
public class SettingsPanel extends JPanel {

    public SettingsPanel() {
        this.setSize(new Dimension(Const.SETTINGS_WIDTH, Const.SETTINGS_HEIGHT));
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        this.setBackground(Color.GRAY);
        this.add(new JButton("设置"));
        this.add(new JButton("退出"));
        this.add(new CreateMatchButton());
        this.add(new DestroyMatchButton());
        this.add(new LogButton());
        this.add(new JButton("GM"));
    }


}
