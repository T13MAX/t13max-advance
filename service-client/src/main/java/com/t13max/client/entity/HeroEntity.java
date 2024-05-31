package com.t13max.client.entity;

import battle.entity.FightHeroInfoPb;
import battle.event.entity.BattleMoveBar;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 19:20 2024/5/29
 */
@Getter
@Setter
public class HeroEntity implements IEntity {

    private long heroId;
    //英雄模板id
    private int templateId;

    private int index;
    //其他参数
    Map<Integer, Double> attrMap = new HashMap<>();
    //行动条信息
    private MoveBarEntity moveBar;


    public HeroEntity(FightHeroInfoPb fightHeroInfoPb, int index) {
        this.heroId = fightHeroInfoPb.getHeroId();
        this.templateId = fightHeroInfoPb.getTemplateId();
        this.attrMap.putAll(fightHeroInfoPb.getAttrMapMap());
        this.moveBar = new MoveBarEntity(fightHeroInfoPb.getMoveBar());
        this.index = index;
    }

    @Override
    public void onChange() {

    }
}
