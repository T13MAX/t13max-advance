package com.t13max.fight.msg;

import battle.api.LoginBattleReq;
import battle.api.LoginBattleResp;
import com.t13max.data.dao.SqlLiteUtil;
import com.t13max.data.entity.AccountData;
import com.t13max.fight.member.IFightMember;
import com.t13max.game.msg.ErrorCode;
import com.t13max.game.msg.IMessage;
import com.t13max.game.msg.Message;
import com.t13max.game.session.ISession;
import message.id.MessageId;

/**
 * 登录消息 比较特殊 直接实现Message接口
 *
 * @author: t13max
 * @since: 14:48 2024/5/29
 */
@Message(MessageId.C_BATTLE_LOGIN_VALUE)
public class LoginBattleMessage implements IMessage<LoginBattleReq> {

    @Override
    public void doMessage(ISession session, int msgId, LoginBattleReq message) {

        AccountData accountData = SqlLiteUtil.selectAccount(message.getUsername());
        if (accountData == null) {
            session.sendError(MessageId.S_BATTLE_LOGIN_VALUE, ErrorCode.FAIL);
            return;
        }
        if (!accountData.getPassword().equals(message.getPassword())) {
            session.sendError(MessageId.S_BATTLE_LOGIN_VALUE, ErrorCode.FAIL);
            return;
        }
        LoginBattleResp.Builder builder = LoginBattleResp.newBuilder();
        builder.setUuid(accountData.getId());
        session.setUuid(accountData.getId());
        session.sendMessage(MessageId.S_BATTLE_LOGIN_VALUE, builder.build());
    }
}
