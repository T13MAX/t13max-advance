package com.t13max.fight.msg;

import com.google.protobuf.MessageLite;
import com.t13max.fight.member.FightMemberManager;
import com.t13max.fight.member.IFightMember;
import com.t13max.game.msg.IMessage;
import com.t13max.game.session.ISession;
import com.t13max.util.Log;

/**
 * @author: t13max
 * @since: 11:10 2024/5/24
 */
public abstract class AbstractMessage<T extends MessageLite> implements IMessage<T> {

    @Override
    public final void doMessage(ISession session, int msgId, T message) {
        Log.common.info("receiveMessage, uuid={}, msgId={}, message={}", session.getUuid(), msgId, message, getClass().getSimpleName());

        IFightMember playerMember = FightMemberManager.inst().getPlayerMember(session.getUuid());
        if (playerMember == null) {
            Log.common.error("member不存在, uuid={}", session.getUuid());
            return;
        }
        doMessage(playerMember, msgId, message);
    }

    protected abstract void doMessage(IFightMember member, int msgId, T Message);
}
