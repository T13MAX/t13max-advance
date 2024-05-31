package com.t13max.client.view.window;

import com.t13max.client.view.enums.CloseAction;
import com.t13max.client.view.enums.Const;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * @author: t13max
 * @since: 13:14 2024/5/30
 */
public class LogWindow extends AbstractWindow {

    public LogWindow() {
        super("日志", new Dimension(Const.LOG_WIDTH, Const.LOG_HEIGHT), false);
    }

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    protected void initWindowContent() {
        this.setLocationRelativeTo(null);
        setDefaultCloseAction(CloseAction.DISPOSE);
        JScrollPane jScrollPane = new JScrollPane();
        this.addComponent("log.scroll", jScrollPane, panel -> {
            this.addComponent(jScrollPane, "log.scroll.table", createTable());
        });
    }

    private JTable createTable() {
        // 表头（列名）
        Object[] columnNames = {"回合", "出手英雄", "技能", "目标英雄", "xxx", "xxx"};

        // 表格所有行数据
        Object[][] rowData = new Object[][]{{"1", "1", "1", "1", "1", "1"}, {"11", "11", "11", "11", "11", "11"}};//orderInforService.findOrder(uname);

        TableModel tableModel = new DefaultTableModel(rowData, columnNames);

        // 使用 表格模型 创建 表格

        JTable table = new JTable(tableModel) {
            //禁止单元格编辑
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        //设置滚动面板
        table.setPreferredScrollableViewportSize(new Dimension(Const.LOG_WIDTH, Const.LOG_HEIGHT));

        table.setShowGrid(true);
        JTableHeader jTableHeader = table.getTableHeader();
        //设置表头是否可移动
        jTableHeader.setReorderingAllowed(true);

        table.setSelectionBackground(Color.lightGray);
        //设置指定列的宽度
        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(50);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);
        table.getColumnModel().getColumn(5).setPreferredWidth(110);
        //table.getColumnModel().getColumn(6).setPreferredWidth(110);
        return table;
    }
}
