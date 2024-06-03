package com.t13max.client.view.window;

import com.t13max.client.entity.HeroEntity;
import com.t13max.client.player.Player;
import com.t13max.client.view.enums.CloseAction;
import com.t13max.client.view.enums.Const;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author: t13max
 * @since: 18:03 2024/5/31
 */
@Getter
public class HeroDetailWindow extends AbstractWindow {

    public HeroDetailWindow() {
        super("英雄详细信息", new Dimension(150, 150), false);
        initWindowContent();
    }

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    protected void initWindowContent() {
        //Point point = new Point();
        //this.setLocation(point);
        this.setDefaultCloseAction(CloseAction.DISPOSE);
        //this.addComponent("11",new Button("1111111111"),null);
        this.addComponent("hero.detail.panel", new JPanel(), panel -> {
            panel.setSize(150, 150);
            //panel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
            panel.setBackground(Color.GRAY);
            JTextArea jTextArea = new JTextArea("3333333333333333");
            jTextArea.setLineWrap(true);
            this.addComponent(panel, "hero.detail.panel.label", jTextArea);
        });
        setUndecorated(true);
    }

    public void update(HeroEntity heroEntity) {
        if (heroEntity == null) {
            return;
        }

        Point location = MouseInfo.getPointerInfo().getLocation();
        this.setBounds((int) location.getX() + 1, (int) location.getY() + 1, 150, 150);
        //显示详细信息
        JTextArea label = getComponent("hero.detail.panel.label");
        if (label == null) {
            return;
        }
        label.setText(heroEntity.toString());
        //this.repaint();
    }
}
