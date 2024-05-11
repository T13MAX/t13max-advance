package com.t13max.fight.member;

import lombok.Data;

/**
 * @author: t13max
 * @since: 14:29 2024/4/16
 */
@Data
public class FightMember {

    private long uid;

    private boolean attacker;

    public FightMember(long uid, boolean attacker) {
        this.uid = uid;
        this.attacker = attacker;
    }
}
