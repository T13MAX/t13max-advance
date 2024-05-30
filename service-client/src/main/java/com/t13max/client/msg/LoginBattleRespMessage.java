package com.t13max.client.msg;

import battle.api.LoginBattleResp;
import com.t13max.client.player.Player;
import com.t13max.client.view.login.LoginFrame;
import com.t13max.game.msg.Message;
import message.id.MessageId;

/**
 * @author: t13max
 * @since: 17:35 2024/5/30
 */
@Message(value = MessageId.S_BATTLE_LOGIN_VALUE)
public class LoginBattleRespMessage extends AbstractMessage<LoginBattleResp> {

    @Override
    public void doMessage(Player player, int msgId, LoginBattleResp message) {
        long uuid = message.getUuid();
        if (uuid == 0) {
            loginFail(-1);
        } else {
            loginSuccess(player,uuid);
        }
    }

    @Override
    public void doErrorMessage(Player player, int msgId, int resCode) {
        loginFail(resCode);
    }


    private void loginFail(int resCode) {
        LoginFrame.LOGIN_FRAME.loginFail("resCode=" + resCode);
    }

    private void loginSuccess(Player player, long uuid) {
        player.setUuid(uuid);
        LoginFrame.LOGIN_FRAME.loginSuccess();
    }

}
