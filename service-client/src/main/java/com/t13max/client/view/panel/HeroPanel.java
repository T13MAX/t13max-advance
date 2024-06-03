package com.t13max.client.view.panel;

import com.t13max.client.entity.HeroEntity;
import com.t13max.client.player.Player;
import com.t13max.client.view.button.PlaceholderButton;
import com.t13max.client.view.enums.Const;
import com.t13max.client.view.label.AttrLabel;
import com.t13max.client.view.window.AbstractWindow;
import com.t13max.client.view.window.HeroDetailWindow;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 13:34 2024/5/30
 */
@Getter
@Setter
public class HeroPanel extends JPanel {

    private HeroEntity heroEntity;

    private Map<String, Component> componentMap = new HashMap<>();

    public HeroPanel() {
        //this.setSize(new Dimension(Const.HERO_LENGTH, Const.HERO_LENGTH));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        init();
    }

    private void init() {

        this.add(new PlaceholderButton());
        addComponent(Const.HP, new AttrLabel(Const.HP));
        addComponent(Const.ATK, new AttrLabel(Const.ATK));
        addComponent(Const.DEF, new AttrLabel(Const.DEF));
        addComponent(Const.SKILL, new AttrLabel(Const.SKILL));
        addComponent(Const.BUFF, new AttrLabel(Const.BUFF));
        PlaceholderButton bottomButton = new PlaceholderButton();
        this.add(bottomButton);
        bottomButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Player.PLAYER.openWindow("detail", false);
                HeroDetailWindow detailWindow = Player.PLAYER.getWindow("detail");

                if (heroEntity != null) {
                    detailWindow.update(heroEntity);
                    detailWindow.openWindow();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                HeroDetailWindow detailWindow = Player.PLAYER.getWindow("detail");
                detailWindow.closeWindow();
            }
        });
    }

    private void addComponent(String name, Component component) {
        this.componentMap.put(name, component);
        this.add(component);
    }

    public final <T extends Component> T getComponent(String componentName) {
        return (T) componentMap.get(componentName);
    }

}
