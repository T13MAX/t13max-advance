package com.t13max.client.view.button;

import com.t13max.client.player.Player;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author: t13max
 * @since: 17:12 2024/6/5
 */
public class SkillButton extends JButton {

    private int index;

    public SkillButton(int index) {

        this.setText(getName(index));
        this.index = index;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Player.PLAYER.setSkillIndex(index);
            }
        });
    }

    private String getName(int index) {
        switch (index) {
            case 0:
                return "小技能";
            case 1:
                return "大招";
            default:
                return "";
        }
    }
}
