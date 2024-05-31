package com.t13max.fight.member;

import com.google.protobuf.MessageLite;
import com.t13max.fight.FightContext;
import com.t13max.game.session.ISession;
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
    public void sendMsg(int msgId, MessageLite messageLite) {
        session.sendMessage(msgId,messageLite);
    }
}
