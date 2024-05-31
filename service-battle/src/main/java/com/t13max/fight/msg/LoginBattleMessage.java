package com.t13max.fight.msg;

import battle.api.LoginBattleReq;
import battle.api.LoginBattleResp;
import com.t13max.data.dao.SqlLiteUtil;
import com.t13max.data.entity.AccountData;
import com.t13max.data.util.UuidUtil;
import com.t13max.game.msg.*;
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
    public void doMessage(ISession session, MessagePack<LoginBattleReq> messagePack) {
        int msgId = messagePack.getMsgId();
        LoginBattleReq message = messagePack.getMessageLite();
        AccountData accountData = SqlLiteUtil.selectAccount(message.getUsername());
        if (accountData == null) {
            //session.sendError(MessageId.S_BATTLE_LOGIN_VALUE, ErrorCode.FAIL);
            //为空自动注册
            accountData = createAccountData(message);
        }
        if (!accountData.getPassword().equals(message.getPassword())) {
            session.sendError(MessageId.S_BATTLE_LOGIN_VALUE, ErrorCode.FAIL);
            return;
        }
        long uuid = accountData.getId();
        LoginBattleResp.Builder builder = LoginBattleResp.newBuilder();
        builder.setUuid(uuid);
        session.setUuid(uuid);
        session.sendMessage(MessageId.S_BATTLE_LOGIN_VALUE, builder.build());
    }

    private AccountData createAccountData(LoginBattleReq message) {
        AccountData accountData = new AccountData();
        accountData.setId(UuidUtil.getNextId());
        accountData.setUsername(message.getUsername());
        accountData.setPassword(message.getPassword());
        SqlLiteUtil.insertAccount(accountData);
        return accountData;
    }

}
