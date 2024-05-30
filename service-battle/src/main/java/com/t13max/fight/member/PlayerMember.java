package com.t13max.fight.member;

import com.google.protobuf.MessageLite;
import com.t13max.fight.FightContext;

/**
 * @author: t13max
 * @since: 17:44 2024/5/28
 */
public class PlayerMember extends FightBaseMember {

    public PlayerMember(FightContext fightContext, long uid, boolean attacker) {
        this.uid = uid;
        this.attacker = attacker;
        this.fightContext = fightContext;
    }

    public PlayerMember(long uid) {
        this.uid = uid;
    }

    @Override
    public void sendMsg(MessageLite messageLite) {

    }
}
