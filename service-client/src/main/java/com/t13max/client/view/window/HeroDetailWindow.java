package com.t13max.client.view.window;

import com.t13max.client.entity.HeroEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * @author: t13max
 * @since: 18:03 2024/5/31
 */
public class HeroDetailWindow extends AbstractWindow {

    public HeroDetailWindow() {
        super("英雄详细信息", new Dimension(), false);
        this.addComponent("hero.detail.panel", new JPanel(), panel -> {
            this.addComponent(panel, "hero.detail.panel.label", new JLabel("3333333333333333"));
        });
        setUndecorated(true);
    }

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    protected void initWindowContent() {

    }

    public void update(HeroEntity heroEntity, int x, int y) {
        if (heroEntity == null) {
            return;
        }
        this.setBounds(x, y, 100, 100);
        //显示详细信息
        JLabel label = getComponent("hero.detail.panel.label");
        if (label == null) {
            return;
        }
        label.setText(heroEntity.toString());
        //this.repaint();
    }
}
