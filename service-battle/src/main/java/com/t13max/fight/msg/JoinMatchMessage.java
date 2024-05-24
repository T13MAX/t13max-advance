package com.t13max.fight.msg;

import battle.api.JoinFightMatchReq;
import com.t13max.fight.FightMatch;
import com.t13max.fight.MatchManager;
import com.t13max.game.msg.ISession;
import com.t13max.util.Log;

/**
 * @author: t13max
 * @since: 19:39 2024/5/23
 */
public class JoinMatchMessage extends AbstractMessage<JoinFightMatchReq> {

    @Override
    public void doMessage(ISession session, int msgId, JoinFightMatchReq message) {
        long uuid = session.getUuid();
        long matchId = message.getMatchId();
        FightMatch fightMatch = MatchManager.inst().getFightMatch(matchId);
        if (fightMatch == null) {
            Log.battle.error("比赛不存在, uuid={}, matchId={}", uuid, matchId);
            return;
        }

        //xxxx
    }
}
