package com.t13max.client.view.battle;

import com.t13max.client.view.Const;

import javax.swing.*;
import java.awt.*;

/**
 * @author: t13max
 * @since: 17:33 2024/5/29
 */
public class BattlePanel extends JSplitPane {

    public BattlePanel() {
        this.setBackground(Color.lightGray);
        this.setSize(Const.GAME_WIDTH, Const.GAME_HEIGHT);
        this.setOrientation(JSplitPane.VERTICAL_SPLIT);
        this.setLeftComponent(createTargetPanel());
        this.setRightComponent(createSelfPanel());
    }

    private JPanel createSelfPanel() {

        JPanel jPanel = new JPanel();
        //jPanel.setBackground(Color.blue);
        jPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        jPanel.add(new HeroPanel());
        jPanel.add(new HeroPanel());
        jPanel.add(new HeroPanel());
        jPanel.add(new HeroPanel());
        jPanel.add(new HeroPanel());
        return jPanel;
    }

    private JPanel createTargetPanel() {
        JPanel jPanel = new JPanel();
        //jPanel.setBackground(Color.red);
        jPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        jPanel.add(new HeroPanel());
        jPanel.add(new HeroPanel());
        jPanel.add(new HeroPanel());
        jPanel.add(new HeroPanel());
        jPanel.add(new HeroPanel());
        return jPanel;
    }
}

