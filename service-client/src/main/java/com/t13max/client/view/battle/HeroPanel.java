package com.t13max.client.view.battle;

import com.t13max.client.view.Const;

import javax.swing.*;
import java.awt.*;

/**
 * @author: t13max
 * @since: 13:34 2024/5/30
 */
public class HeroPanel extends JPanel {

    public HeroPanel() {
        this.setSize(new Dimension(Const.HERO_LENGTH, Const.HERO_LENGTH));
        this.add(new JLabel("某英雄"));
    }
}
