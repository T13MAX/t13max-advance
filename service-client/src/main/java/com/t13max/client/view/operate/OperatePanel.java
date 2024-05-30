package com.t13max.client.view.operate;

import com.t13max.client.view.settings.SettingsPanel;

import javax.swing.*;

/**
 * @author: t13max
 * @since: 11:40 2024/5/30
 */
public class OperatePanel extends JSplitPane {

    public OperatePanel() {
        this.setOrientation(JSplitPane.VERTICAL_SPLIT);
        this.setDividerLocation(70);
        this.setDividerSize(10);
        //设置ui小部件
        this.setOneTouchExpandable(true);
        this.setContinuousLayout(true);

        this.setBottomComponent(new JPanel());
        this.setTopComponent(new SettingsPanel());

        this.setEnabled(false);
    }

    private JTextArea createTextArea() {
        JTextArea jTextArea = new JTextArea(100,10);
        jTextArea.setText("3333333333333333333333333333333333");
        return jTextArea;
    }
}
