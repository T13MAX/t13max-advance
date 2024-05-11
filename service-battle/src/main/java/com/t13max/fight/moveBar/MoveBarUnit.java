package com.t13max.fight.moveBar;

import lombok.Data;

/**
 * @author: t13max
 * @since: 11:57 2024/4/11
 */
@Data
public class MoveBarUnit {

    private long heroId;

    private boolean attacker;

    private int speed;

    private float curDistance;

    private int actionDistance;

    public MoveBarUnit(long heroId, boolean attacker, int speed, float curDistance, int actionDistance) {
        this.heroId = heroId;
        this.attacker = attacker;
        this.speed = speed;
        this.curDistance = curDistance;
        this.actionDistance = actionDistance;
    }

    public boolean canAction() {
        return this.curDistance >= this.actionDistance;
    }

    public void doAction() {
        this.curDistance -= this.actionDistance;
    }

}
