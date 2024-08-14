package com.t13max.fight.msg;

import battle.api.JoinFightMatchReq;
import battle.api.JoinFightMatchResp;
import battle.entity.FightMatchPb;
import com.t13max.common.msg.Message;
import com.t13max.fight.context.FightMatch;
import com.t13max.fight.context.MatchManager;
import com.t13max.fight.member.IFightMember;
import com.t13max.fight.member.PlayerMember;
import com.t13max.game.session.BattleSession;
import com.t13max.game.util.Log;
import message.id.MessageId;

/**
 * @author: t13max
 * @since: 19:39 2024/5/23
 */
@Message(MessageId.C_JOIN_MATCH_VALUE)
public class JoinMatchMessage extends AbstractMessage<JoinFightMatchReq> {

    @Override
    protected void doMessage(BattleSession battleSession, int msgId, JoinFightMatchReq message) {

        long matchId = message.getMatchId();
        long uuid = battleSession.getUuid();
        FightMatch fightMatch = MatchManager.inst().getFightMatch(matchId);
        if (fightMatch == null) {
            Log.battle.error("比赛不存在, uuid={}, matchId={}", uuid, matchId);
            return;
        }
        battleSession.setMatchId(matchId);
        IFightMember fightMember = fightMatch.getMemberMap().get(uuid);
        if (fightMember == null) {
            Log.battle.error("fightMember不存在, uuid={}, matchId={}", uuid, matchId);
            return;
        }
        if (!(fightMember instanceof PlayerMember playerMember)) {
            Log.battle.error("playerMember不存在, uuid={}, matchId={}", uuid, matchId);
            return;
        }
        playerMember.setSession(battleSession);
        JoinFightMatchResp.Builder builder = JoinFightMatchResp.newBuilder();
        FightMatchPb fightMatchPb = fightMatch.buildFightMatchPb();
        builder.setFightMatchPb(fightMatchPb);
        battleSession.sendMessage(MessageId.S_JOIN_MATCH_VALUE, builder.build());
    }
}
