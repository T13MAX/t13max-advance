package com.t13max.fight.msg;

import battle.api.CreateFightMatchReq;
import battle.api.CreateFightMatchResp;
import com.t13max.fight.FightFactory;
import com.t13max.fight.FightMatch;
import com.t13max.fight.MatchManager;
import com.t13max.fight.member.IFightMember;
import com.t13max.game.session.ISession;
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
    protected void doMessage(IFightMember member, int msgId, CreateFightMatchReq message) {
        FightMatch fightMatch = FightFactory.createFightMatch(message);

        if (fightMatch == null) {
            Log.battle.error("战斗创建失败");
            return;
        }

        MatchManager.inst().addFightMatch(fightMatch);

        CreateFightMatchResp.Builder builder = CreateFightMatchResp.newBuilder();
        member.sendMsg(builder.build());
    }
}
