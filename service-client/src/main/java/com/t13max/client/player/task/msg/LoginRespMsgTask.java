package com.t13max.client.player.task.msg;

import battle.api.LoginBattleResp;
import com.t13max.client.player.Player;
import com.t13max.client.player.task.AbstractTask;
import com.t13max.client.view.login.LoginFrame;
import com.t13max.util.Log;
import message.id.MessageId;

import java.util.List;

/**
 * @author: t13max
 * @since: 13:53 2024/5/30
 */
public class LoginRespMsgTask extends AbstractTask {

    @Override
    public void run() {
        List<MessagePack<LoginBattleResp>> messagePacks = Player.PLAYER.getMessage(MessageId.S_BATTLE_LOGIN_VALUE);
        if (messagePacks == null || messagePacks.isEmpty()) {
            retry();
            return;
        }

        if (messagePacks.size() != 1) {
            Log.client.error("重复登录!");
        }

        MessagePack<LoginBattleResp> messagePack = messagePacks.get(0);

        if (messagePack.isError()) {
            loginFail(messagePack.getResCode());
            return;
        }
        LoginBattleResp loginBattleResp = messagePack.getMessage();
        long uuid = loginBattleResp.getUuid();
        if (uuid == 0) {
            loginFail(-1);
        } else {
            loginSuccess(uuid);
        }

    }

    private void loginFail(int resCode) {
        LoginFrame.LOGIN_FRAME.loginFail("resCode=" + resCode);
    }

    private void loginSuccess(long uuid) {
        this.player.setUuid(uuid);
        LoginFrame.LOGIN_FRAME.loginSuccess();
    }

}
