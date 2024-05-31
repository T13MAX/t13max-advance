package com.t13max.client.view.window;

import com.t13max.client.player.Player;
import com.t13max.client.view.window.AbstractWindow;
import com.t13max.client.view.panel.BattlePanel;
import com.t13max.client.view.enums.Const;
import com.t13max.client.view.enums.CloseAction;
import com.t13max.client.view.panel.OperatePanel;

import javax.swing.*;
import java.awt.*;

/**
 * 主界面
 *
 * @author: t13max
 * @since: 15:36 2024/5/29
 */
public class HomeWindow extends AbstractWindow {

    public HomeWindow() {
        super(Const.MAIN_FRAME_TITLE + " - " + Player.PLAYER.getUuid(), new Dimension(Const.MAIN_WIDTH, Const.MAIN_HEIGHT), false);
        setDefaultCloseAction(CloseAction.EXIT);
        initWindowContent();
    }

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    protected void initWindowContent() {

        JSplitPane mainPanel = new JSplitPane();

        //默认水平分割
        this.addComponent("home.main", mainPanel, BorderLayout.CENTER, panel -> {
            //设置左面大小
            panel.setDividerLocation(Const.GAME_WIDTH);
            //设置分割线大小
            panel.setDividerSize(10);
            //设置不可改变
            panel.setEnabled(false);
            //左边组件
            //panel.setLeftComponent(battlePanel);
            //右边组件
            //panel.setRightComponent(operatePanel);
        });

        //初始化战斗panel
        this.addComponent(mainPanel, "home.main.battle", new BattlePanel(this), JSplitPane.LEFT);
        //添加操作panel
        this.addComponent(mainPanel, "home.main.operate", new OperatePanel(this), JSplitPane.RIGHT);
    }


}
