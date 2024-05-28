package com.t13max.fight.msg;

import lombok.Data;

import java.util.List;

/**
 * 行动参数对象
 *
 * @author: t13max
 * @since: 15:19 2024/4/15
 */
@Data
public class DoActionArgs {

    private long playerId;
    private long heroId;
    private int skillId;
    private List<Long> targetIds;

    public DoActionArgs(long playerId, long heroId) {
        this.playerId = playerId;
        this.heroId = heroId;
    }
}
