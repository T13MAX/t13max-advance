package com.t13max.fight.msg;

import battle.api.DestroyFightMatchReq;
import battle.api.DestroyFightMatchResp;
import com.t13max.fight.FightMatch;
import com.t13max.fight.MatchManager;
import com.t13max.game.msg.Message;
import com.t13max.game.session.BattleSession;
import com.t13max.util.Log;
import message.id.MessageId;

/**
 * 理论上 销毁战斗不是客户端调用的 应该是game服rpc过来的
 *
 * @author: t13max
 * @since: 19:39 2024/5/23
 */
@Message(MessageId.C_DESTROY_MATCH_VALUE)
public class DestroyMatchMessage extends AbstractMessage<DestroyFightMatchReq> {


    @Override
    protected void doMessage(BattleSession battleSession, int msgId, DestroyFightMatchReq message) {
        long uuid = battleSession.getUuid();
        long matchId = message.getMatchId();
        FightMatch fightMatch = MatchManager.inst().getFightMatch(matchId);
        if (fightMatch == null) {
            Log.battle.error("fightMatch不存在, id={}", matchId);
            return;
        }
        fightMatch.forceStop();
        battleSession.sendMessage(MessageId.S_DESTROY_MATCH_VALUE, DestroyFightMatchResp.getDefaultInstance());
    }
}
