package com.t13max.client.entity;

import battle.entity.FightHeroInfoPb;
import battle.event.entity.BattleMoveBar;
import com.t13max.client.player.Player;
import com.t13max.client.view.enums.AttrEnum;
import com.t13max.client.view.panel.BattlePanel;
import com.t13max.client.view.panel.HeroPanel;
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
        HomeWindow homeWindow = Player.PLAYER.getWindow("home");

        HeroPanel heroPanel = homeWindow.getComponent(getName());
        if (heroPanel == null) {
            Log.client.error("未找到对应panel. name={}");
            return;
        }
        for (Map.Entry<Integer, Double> entry : this.attrMap.entrySet()) {

            AttrEnum attrEnum = AttrEnum.getAttrEnum(entry.getKey());
            if (attrEnum == null || !attrEnum.hasName()) {
                continue;
            }
            JLabel jLabel = heroPanel.getComponent(attrEnum.getName());
            jLabel.setText(attrEnum.getName() + ": " + String.format("%.2f", entry.getValue()));
            jLabel.repaint();
        }
        heroPanel.setHeroEntity(this);
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
}
