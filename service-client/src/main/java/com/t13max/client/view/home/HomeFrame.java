package com.t13max.client.view.home;

import com.t13max.client.view.battle.BattlePanel;
import com.t13max.client.view.Const;
import com.t13max.client.view.operate.OperatePanel;

import javax.swing.*;
import java.awt.*;
/**
 * 主界面
 *
 * @author: t13max
 * @since: 15:36 2024/5/29
 */
public class HomeFrame extends JFrame {

    public static JSplitPane operatePanel = new OperatePanel();
    public static JSplitPane battlePanel = new BattlePanel();

    public HomeFrame() {
        init();
        this.setTitle(Const.MAIN_FRAME_TITLE);
        this.setSize(Const.MAIN_WIDTH, Const.MAIN_HEIGHT);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void init() {
        Container container = this.getContentPane();
        //默认水平分割
        JSplitPane splitPane = new JSplitPane();
        //设置左面大小
        splitPane.setDividerLocation(Const.GAME_WIDTH);
        //设置分割线大小
        splitPane.setDividerSize(10);
        //设置不可改变
        splitPane.setEnabled(false);
        //左边组件
        splitPane.setLeftComponent(battlePanel);
        //右边组件
        splitPane.setRightComponent(operatePanel);
        container.add(splitPane, BorderLayout.CENTER);
    }
}
