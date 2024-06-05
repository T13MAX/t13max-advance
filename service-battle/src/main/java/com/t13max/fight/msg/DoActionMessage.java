package com.t13max.fight.msg;

import battle.api.DoActionReq;
import com.t13max.fight.context.FightMatch;
import com.t13max.fight.context.MatchManager;
import com.t13max.game.msg.Message;
import com.t13max.game.session.BattleSession;
import com.t13max.util.Log;
import message.id.MessageId;

/**
 * @author: t13max
 * @since: 16:37 2024/6/5
 */
@Message(value = MessageId.C_MATCH_ACTION_VALUE)
public class DoActionMessage extends AbstractMessage<DoActionReq>{

    @Override
    protected void doMessage(BattleSession battleSession, int msgId, DoActionReq message) {
        long uuid = battleSession.getUuid();
        long matchId = battleSession.getMatchId();
        FightMatch fightMatch = MatchManager.inst().getFightMatch(matchId);
        if (fightMatch == null) {
            Log.battle.error("fightMatch不存在, id={}", matchId);
            return;
        }
        DoActionArgs doActionArgs = new DoActionArgs(message);
        fightMatch.setDoActionArgs(doActionArgs);
    }
}
