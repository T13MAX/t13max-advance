package com.t13max.fight.member;

import com.google.protobuf.MessageLite;
import com.t13max.fight.context.FightContext;

/**
 * @author: t13max
 * @since: 17:44 2024/5/28
 */
public class MonsterMember extends FightBaseMember {

    public MonsterMember(FightContext fightContext, long uid, boolean attacker) {
        this.uid = uid;
        this.attacker = attacker;
        this.fightContext = fightContext;
    }

    @Override
    public void sendMsg(int msgId, MessageLite messageLite) {

    }


}
