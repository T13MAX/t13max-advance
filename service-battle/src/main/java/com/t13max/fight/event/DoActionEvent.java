package com.t13max.fight.event;

import lombok.Getter;

import java.util.List;

/**
 * @author: t13max
 * @since: 16:10 2024/4/15
 */
@Getter
public class DoActionEvent extends AbstractEvent {

    private long heroId;

    private boolean attacker;

    private int skillId;

    private List<Long> targetIds;

    public DoActionEvent(long heroId, boolean attacker, int skillId, List<Long> targetIds) {
        super(FightEventEnum.DO_ACTION);
        this.heroId = heroId;
        this.skillId = skillId;
        this.targetIds = targetIds;
        this.attacker = attacker;
    }
}
