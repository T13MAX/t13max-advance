package com.t13max.client.entity;

import battle.event.entity.BattleMoveBar;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: t13max
 * @since: 20:52 2024/5/30
 */
@Getter
@Setter
public class MoveBarEntity implements IEntity {
    private long heroId; // 英雄id
    private int speed; // 速度值
    private int totalDistance; // 总距离
    private float currDistance; // 当前距离

    public MoveBarEntity(BattleMoveBar moveBar) {
        this.heroId = moveBar.getHeroId();
        this.speed = moveBar.getSpeed();
        this.totalDistance = moveBar.getTotalDistance();
        this.currDistance = moveBar.getCurrDistance();
    }

    @Override
    public void onChange() {

    }
}
