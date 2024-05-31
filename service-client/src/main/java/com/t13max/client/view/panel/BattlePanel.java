package com.t13max.client.view.panel;

import com.t13max.client.view.enums.Const;
import com.t13max.client.view.window.HomeWindow;

import javax.swing.*;
import java.awt.*;

/**
 * @author: t13max
 * @since: 17:33 2024/5/29
 */
public class BattlePanel extends JSplitPane {


    public BattlePanel(HomeWindow homeWindow) {
        this.setBackground(Color.lightGray);
        this.setSize(Const.GAME_WIDTH, Const.GAME_HEIGHT);
        this.setOrientation(JSplitPane.VERTICAL_SPLIT);

        init(homeWindow);
    }

    private void init(HomeWindow homeWindow) {
        homeWindow.addComponent(this, "home.main.battle.target", new JPanel(), JSplitPane.TOP, panel -> {
            //jPanel.setBackground(Color.blue);
            panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
            initHero(homeWindow, "target", panel);
        });
        homeWindow.addComponent(this, "home.main.battle.self", new JPanel(), JSplitPane.BOTTOM, panel -> {
            //jPanel.setBackground(Color.blue);
            panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
            initHero(homeWindow, "self", panel);
        });
    }

    private static void initHero(HomeWindow homeWindow, String side, JPanel panel) {
        homeWindow.addComponent(panel, "home.main.battle." + side + ".hero.0", new HeroPanel());
        homeWindow.addComponent(panel, "home.main.battle." + side + ".hero.1", new HeroPanel());
        homeWindow.addComponent(panel, "home.main.battle." + side + ".hero.2", new HeroPanel());
        homeWindow.addComponent(panel, "home.main.battle." + side + ".hero.3", new HeroPanel());
        homeWindow.addComponent(panel, "home.main.battle." + side + ".hero.4", new HeroPanel());
    }
}

