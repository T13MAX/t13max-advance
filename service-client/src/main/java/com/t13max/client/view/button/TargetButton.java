package com.t13max.client.view.button;

import com.t13max.client.player.Player;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author: t13max
 * @since: 17:10 2024/6/5
 */
public class TargetButton extends JButton {

    private int index;

    public TargetButton(int index) {
        super(String.valueOf(index));
        this.index = index;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Player player = Player.PLAYER;
                player.setTargetIndex(index);
                player.doAction();
            }
        });
    }
}
