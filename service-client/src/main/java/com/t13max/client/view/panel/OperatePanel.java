package com.t13max.client.view.panel;

import com.t13max.client.view.window.HomeWindow;

import javax.swing.*;

/**
 * @author: t13max
 * @since: 11:40 2024/5/30
 */
public class OperatePanel extends JSplitPane {

    public OperatePanel(HomeWindow homeWindow) {
        this.setOrientation(JSplitPane.VERTICAL_SPLIT);
        this.setDividerLocation(70);
        this.setDividerSize(10);
        //设置ui小部件
        this.setOneTouchExpandable(true);
        this.setContinuousLayout(true);

        this.setBottomComponent(new SkillPanel());
        this.setTopComponent(new SettingsPanel());

        this.setEnabled(false);
    }

}
