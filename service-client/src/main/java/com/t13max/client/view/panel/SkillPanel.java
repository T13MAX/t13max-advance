package com.t13max.client.view.panel;

import com.t13max.client.view.button.SkillButton;
import com.t13max.client.view.button.TargetButton;
import com.t13max.client.view.enums.Const;

import javax.swing.*;
import java.awt.*;

/**
 * @author: t13max
 * @since: 18:45 2024/6/5
 */
public class SkillPanel extends JPanel {

    public SkillPanel() {
        this.setSize(new Dimension(Const.SETTINGS_WIDTH, 0));
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        this.setBackground(Color.GRAY);

        for (int i = 0; i < 5; i++) {
            this.add(new TargetButton(i));
        }
        this.add(new JButton());
        for (int i = 0; i < 2; i++) {
            this.add(new SkillButton(i));
        }
    }


}
