package com.t13max.game.feature.login;

import com.t13max.game.msg.AbstractMessage;
import com.t13max.game.msg.Message;
import com.t13max.game.player.Player;
import game.login.api.LoginGameReq;
import message.id.MessageId;

/**
 * @author: t13max
 * @since: 11:25 2024/5/24
 */
@Message(MessageId.C_LOGIN_GAME_VALUE)
public class LoginMessage extends AbstractMessage<LoginGameReq> {

    @Override
    public void doMessage(Player player, LoginGameReq message) {

    }

}
