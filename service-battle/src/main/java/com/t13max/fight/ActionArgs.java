package com.t13max.fight;

import lombok.Data;

import java.util.List;

/**
 * @author: t13max
 * @since: 15:19 2024/4/15
 */
@Data
public class ActionArgs {

    private long playerId;
    private long heroId;
    private int skillId;
    private List<Long> targetIds;

    public ActionArgs(long playerId, long heroId) {
        this.playerId = playerId;
        this.heroId = heroId;
    }
}
