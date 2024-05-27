package com.t13max.fight.msg;

import battle.api.CreateFightMatchReq;
import battle.api.CreateFightMatchResp;
import battle.api.JoinFightMatchReq;
import com.t13max.fight.FightFactory;
import com.t13max.fight.FightMatch;
import com.t13max.fight.MatchManager;
import com.t13max.game.msg.ISession;
import com.t13max.game.msg.Message;
import com.t13max.util.Log;
import message.id.MessageId;

/**
 * @author: t13max
 * @since: 19:39 2024/5/23
 */
@Message(MessageId.C_CREATE_MATCH_VALUE)
public class CreateMatchMessage extends AbstractMessage<CreateFightMatchReq> {

    @Override
    public void doMessage(ISession session, int msgId, CreateFightMatchReq message) {
        FightMatch fightMatch = FightFactory.createFightMatch(message);

        MatchManager.inst().addFightMatch(fightMatch);

        CreateFightMatchResp.Builder builder = CreateFightMatchResp.newBuilder();
        session.sendMessage(builder.build());
    }
}
