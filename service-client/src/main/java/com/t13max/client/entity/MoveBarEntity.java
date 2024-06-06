package com.t13max.client.entity;

import battle.entity.FightHeroInfoPb;
import battle.event.entity.BattleMoveBar;
import com.google.protobuf.MessageLite;
import com.t13max.client.player.Player;
import com.t13max.client.view.enums.Const;
import com.t13max.client.view.panel.HeroPanel;
import com.t13max.client.view.progress.ActionProgress;
import com.t13max.client.view.window.HomeWindow;
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

    public void onChange(ActionProgress progress) {
        int value = (int) (currDistance / totalDistance * Const.MAX_PROCESS);
        progress.setValue(value);
    }

    @Override
    public void onChange() {

    }

    @Override
    public void clear() {

    }

    @Override
    public <T extends MessageLite> void update(T t) {
        if (!(t instanceof BattleMoveBar moveBar)) {
            return;
        }
        this.heroId = moveBar.getHeroId();
        this.speed = moveBar.getSpeed();
        this.totalDistance = moveBar.getTotalDistance();
        this.currDistance = moveBar.getCurrDistance();
    }
}
