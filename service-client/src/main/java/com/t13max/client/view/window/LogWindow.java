package com.t13max.client.view.window;

import battle.api.DoActionResp;
import battle.entity.FightEventPb;
import battle.event.entity.*;
import com.t13max.client.player.Player;
import com.t13max.client.view.enums.AttrEnum;
import com.t13max.client.view.enums.CloseAction;
import com.t13max.client.view.enums.Const;
import com.t13max.util.Log;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * @author: t13max
 * @since: 13:14 2024/5/30
 */
public class LogWindow extends AbstractWindow {

    private DefaultTableModel tableModel;

    public LogWindow() {
        super("日志", new Dimension(Const.LOG_WIDTH, Const.LOG_HEIGHT), false);
        initWindowContent();
    }

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    protected void initWindowContent() {
        this.setLocationRelativeTo(null);
        setDefaultCloseAction(CloseAction.DISPOSE);
        JScrollPane jScrollPane = new JScrollPane(createTable());
        this.addComponent("log.scroll", jScrollPane, panel -> {
        });
    }

    private JTable createTable() {
        // 表头（列名）
        Object[] columnNames = {"回合", "事件", "值"};

        // 表格所有行数据
        Object[][] rowData = new Object[][]{};

        tableModel = new DefaultTableModel(rowData, columnNames);

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

        table.setSelectionBackground(Color.red);
        //设置指定列的宽度
        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);
        //table.getColumnModel().getColumn(3).setPreferredWidth(50);
        //table.getColumnModel().getColumn(4).setPreferredWidth(110);
        //table.getColumnModel().getColumn(5).setPreferredWidth(110);
        //table.getColumnModel().getColumn(6).setPreferredWidth(110);
        return table;
    }

    /**
     * 刷新日志
     * 暂时先就打印 不记录
     *
     * @Author t13max
     * @Date 17:02 2024/6/5
     */
    public void update(DoActionResp doActionResp) {
        int round = doActionResp.getRound();
        for (FightEventPb fightEventPb : doActionResp.getEventList()) {
            FightEventPb.EventCase eventCase = fightEventPb.getEventCase();
            String info;
            switch (eventCase) {
                case DOACTIONEVENT -> {
                    DoActionEventPb doActionEvent = fightEventPb.getDoActionEvent();
                    info = doActionEvent.getHeroId() + "对" + doActionEvent.getTargetIdList() + "发动了" + doActionEvent.getSkillId();
                }
                case DAMAGEEVENTPB -> {
                    DamageEventPb damageEventPb = fightEventPb.getDamageEventPb();
                    FightDamage damages = damageEventPb.getDamages();
                    info = "对" + damages.getTargetHeroId() + "造成" + damages.getDamage() + "伤害";
                }
                case CUREEVENTPB -> {
                    CureEventPb cureEventPb = fightEventPb.getCureEventPb();
                    info = "对" + cureEventPb.getTargetHeroId() + "治疗" + cureEventPb.getValue();
                }
                case DEADEVENTPB -> {
                    DeadEventPb deadEventPb = fightEventPb.getDeadEventPb();
                    info = deadEventPb.getDeadHeroId() + "死了";
                }
                case BUFFADDEVENTPB -> {
                    BuffAddEventPb buffAddEventPb = fightEventPb.getBuffAddEventPb();
                    BuffBoxPb buffBox = buffAddEventPb.getBuffBox();
                    info = "挂上了" + buffBox.getBuffId() + "buff, " + "当前状态+" + buffBox.getBuffStatus();
                }
                case BUFFREMOVEEVENTPB -> {
                    BuffRemoveEventPb buffRemoveEventPb = fightEventPb.getBuffRemoveEventPb();
                    BuffBoxPb buffBox = buffRemoveEventPb.getBuffBox();
                    info = buffBox.getOwnerId() + "的buff" + buffBox.getBuffId() + "被移除! reason=" + buffRemoveEventPb.getReason();
                }
                case ATTRIBUTEUPDATEEVENTPB -> {
                    AttributeUpdateEventPb attributeUpdateEventPb = fightEventPb.getAttributeUpdateEventPb();
                    AttrEnum attrEnum = AttrEnum.getAttrEnum(attributeUpdateEventPb.getAttributeType());
                    if (attrEnum == null) {
                        continue;
                    }
                    double delta = attributeUpdateEventPb.getDelta();
                    info = "属性发生变化!" + attributeUpdateEventPb.getHeroId() + "的" + attrEnum.name() + (delta > 0 ? "增加" : "减少") + "了" + Math.abs(delta);
                }
                case MOVEBARUPDATEEVENTPB -> {
                    MoveBarUpdateEventPb moveBarUpdateEventPb = fightEventPb.getMoveBarUpdateEventPb();
                    BattleMoveBar moveBars = moveBarUpdateEventPb.getMoveBars();
                    info = "行动条变化! " + moveBars.getHeroId() + "当前为" + moveBars.getCurrDistance();
                }
                case BUFFUPDATEEVENTPB -> {
                    BuffUpdateEventPb buffUpdateEventPb = fightEventPb.getBuffUpdateEventPb();
                    BuffBoxPb buffBox = buffUpdateEventPb.getBuffBox();
                    info = buffBox.getOwnerId() + "的buff" + buffBox.getBuffId() + "状态更新, 当前为" + buffBox.getBuffStatus();
                }
                case BUFFACTIONEVENTPB -> {
                    BuffActionEventPb buffActionEventPb = fightEventPb.getBuffActionEventPb();
                    BuffBoxPb buffBox = buffActionEventPb.getBuffBox();
                    info = buffBox.getOwnerId() + "的buff" + buffBox.getBuffId() + "生效!";
                }
                default -> {
                    continue;
                }
            }
            String eventName = eventCase.toString();

            tableModel.addRow(new Object[]{round, eventName, info});
        }
    }
}
