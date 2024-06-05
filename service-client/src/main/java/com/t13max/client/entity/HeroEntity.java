package com.t13max.client.entity;

import battle.entity.FightHeroInfoPb;
import battle.entity.FightMatchPb;
import battle.event.entity.BattleMoveBar;
import com.google.protobuf.MessageLite;
import com.t13max.client.player.Player;
import com.t13max.client.view.enums.AttrEnum;
import com.t13max.client.view.enums.Const;
import com.t13max.client.view.panel.BattlePanel;
import com.t13max.client.view.panel.HeroPanel;
import com.t13max.client.view.progress.ActionProgress;
import com.t13max.client.view.progress.HpProgress;
import com.t13max.client.view.window.AbstractWindow;
import com.t13max.client.view.window.HomeWindow;
import com.t13max.util.Log;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 19:20 2024/5/29
 */
@Data
public class HeroEntity implements IEntity {

    private long heroId;
    //英雄模板id
    private int templateId;

    private int index;
    //其他参数
    Map<Integer, Double> attrMap = new HashMap<>();
    //行动条信息
    private MoveBarEntity moveBar;

    private boolean attacker;

    public HeroEntity(FightHeroInfoPb fightHeroInfoPb, boolean attacker, int index) {
        this.heroId = fightHeroInfoPb.getHeroId();
        this.templateId = fightHeroInfoPb.getTemplateId();
        this.attrMap.putAll(fightHeroInfoPb.getAttrMapMap());
        this.moveBar = new MoveBarEntity(fightHeroInfoPb.getMoveBar());
        this.index = index;
        this.attacker = attacker;
    }

    @Override
    public void onChange() {

        HeroPanel heroPanel = this.getPanel();
        if (heroPanel == null) {
            Log.client.error("未找到对应panel. name={}");
            return;
        }

        //属性变化
        onAttrChange(heroPanel);

        //行动条change
        onActionChange(heroPanel);

        heroPanel.setHeroEntity(this);
    }

    @Override
    public <T extends MessageLite> void update(T t) {
        if (!(t instanceof FightHeroInfoPb heroInfoPb)) {
            return;
        }
        this.heroId = heroInfoPb.getHeroId();
        this.templateId = heroInfoPb.getTemplateId();
        this.attrMap.putAll(heroInfoPb.getAttrMapMap());
        this.moveBar.update(heroInfoPb.getMoveBar());
    }

    private void onActionChange(HeroPanel heroPanel) {
        ActionProgress progress = heroPanel.getComponent(Const.ACTION);
        moveBar.onChange(progress);
        progress.repaint();
    }

    private void onAttrChange(HeroPanel heroPanel) {
        for (Map.Entry<Integer, Double> entry : this.attrMap.entrySet()) {

            AttrEnum attrEnum = AttrEnum.getAttrEnum(entry.getKey());
            if (attrEnum == null || !attrEnum.hasName()) {
                continue;
            }

            Double value = entry.getValue();

            switch (attrEnum) {
                //血量进度条处理
                case CUR_HP -> {
                    Double maxHp = this.attrMap.get(AttrEnum.MAX_HP.getId());
                    if (maxHp == null) {
                        maxHp = 100D;
                    }
                    HpProgress progress = heroPanel.getComponent(Const.HP_PROCESS);
                    progress.setValue((int) (value / maxHp) * Const.MAX_PROCESS);
                    progress.repaint();
                    JLabel jLabel = heroPanel.getComponent(attrEnum.getName());
                    jLabel.setText(attrEnum.getName() + ": " + String.format("%.2f", value));
                    jLabel.repaint();
                }
                //其他默认属性
                default -> {
                    JLabel jLabel = heroPanel.getComponent(attrEnum.getName());
                    jLabel.setText(attrEnum.getName() + ": " + String.format("%.2f", value));
                    jLabel.repaint();
                }
            }

        }
    }

    private String getName() {
        return "home.main.battle." + getSide() + ".hero." + index;
    }

    private String getSide() {
        String side;
        if (attacker) {
            side = "self";
        } else {
            side = "target";
        }
        return side;
    }

    public HeroPanel getPanel() {
        HomeWindow homeWindow = Player.PLAYER.getWindow("home");
        HeroPanel heroPanel = homeWindow.getComponent(getName());
        return heroPanel;
    }
}
