package com.t13max.client.view.progress;

import com.t13max.client.view.enums.Const;

import javax.swing.*;
import java.awt.*;

/**
 * @author: t13max
 * @since: 14:36 2024/6/5
 */
public class HpProgress extends JProgressBar {

    public HpProgress() {
        super(JProgressBar.HORIZONTAL, 0, Const.MAX_PROCESS);
        this.setPreferredSize(new Dimension(Const.HERO_WIDTH, 10));
    }
}
