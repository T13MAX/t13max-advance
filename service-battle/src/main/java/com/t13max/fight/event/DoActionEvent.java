package com.t13max.fight.event;

import battle.entity.FightEventPb;
import battle.event.entity.DoActionEventPb;
import lombok.Getter;

import java.util.List;

/**
 * @author: t13max
 * @since: 16:10 2024/4/15
 */
@Getter
public class DoActionEvent extends AbstractEvent implements IFightEventPackager {

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

    @Override
    public FightEventPb pack() {
        FightEventPb.Builder builder = FightEventPb.newBuilder();
        DoActionEventPb.Builder eventBuilder = DoActionEventPb.newBuilder();
        eventBuilder.setHeroId(this.heroId);
        eventBuilder.setSkillId(this.skillId);
        eventBuilder.addAllTargetId(this.targetIds);
        builder.setDoActionEvent(eventBuilder);
        return builder.build();
    }
}
