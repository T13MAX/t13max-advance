package com.t13max.fight.member;

import com.google.protobuf.MessageLite;
import com.t13max.common.session.ISession;
import com.t13max.fight.context.FightContext;
import lombok.Setter;

/**
 * @author: t13max
 * @since: 17:44 2024/5/28
 */
@Setter
public class PlayerMember extends FightBaseMember {

    private ISession session;

    public PlayerMember(FightContext fightContext, long uid, boolean attacker) {
        this.uid = uid;
        this.attacker = attacker;
        this.fightContext = fightContext;
    }

    public PlayerMember(long uid) {
        this.uid = uid;
    }

    @Override
    public boolean isReady() {
        return session != null;
    }

    @Override
    public void sendMsg(int msgId, MessageLite messageLite) {
        session.sendMessage(msgId, messageLite);
    }
}
