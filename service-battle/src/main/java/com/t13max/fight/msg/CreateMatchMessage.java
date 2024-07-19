package com.t13max.fight.msg;

import battle.api.CreateFightMatchReq;
import battle.api.CreateFightMatchResp;
import com.t13max.fight.context.FightFactory;
import com.t13max.fight.context.FightMatch;
import com.t13max.fight.context.MatchManager;
import com.t13max.fight.buff.BuffBoxImpl;
import com.t13max.fight.buff.BuffFactory;
import com.t13max.fight.hero.FightHero;
import com.t13max.game.msg.IMessage;
import com.t13max.game.msg.MessagePack;
import com.t13max.game.session.ISession;
import com.t13max.game.msg.Message;
import com.t13max.game.util.Log;
import message.id.MessageId;

/**
 * 理论上 创建战斗不是客户端调用的 应该是game服rpc过来的
 *
 * @author: t13max
 * @since: 19:39 2024/5/23
 */
@Message(MessageId.C_CREATE_MATCH_VALUE)
public class CreateMatchMessage implements IMessage<CreateFightMatchReq> {

    @Override
    public void doMessage(ISession session, MessagePack<CreateFightMatchReq> messagePack) {

        long uuid = session.getUuid();
        CreateFightMatchReq message = messagePack.getMessageLite();

        FightMatch fightMatch = FightFactory.createFightMatch(message);

        if (fightMatch == null) {
            Log.battle.error("战斗创建失败");
            return;
        }

        //临时测试代码
        for (FightHero fightHero : fightMatch.getHeroMap().values()) {
            if (!fightHero.isAttacker()) {
                continue;
            }
            //给自己挂一个牛逼逼的buff
            BuffBoxImpl buffBoxImpl = BuffFactory.createBuffBoxImpl(fightHero.getFightContext(), fightHero.getId(), 120000);
            fightHero.getBuffManager().addBuff(buffBoxImpl);
        }

        MatchManager.inst().addFightMatch(fightMatch);

        MatchManager.inst().addFightMatch(fightMatch);
        CreateFightMatchResp.Builder builder = CreateFightMatchResp.newBuilder();
        session.sendMessage(MessageId.S_CREATE_MATCH_VALUE,builder.build());
    }
}
