package com.t13max.client.msg;

import battle.api.DoActionResp;
import battle.api.JoinFightMatchResp;
import battle.entity.FightMatchPb;
import com.t13max.client.entity.MatchEntity;
import com.t13max.client.player.Player;
import com.t13max.client.view.window.HomeWindow;
import com.t13max.client.view.window.LogWindow;
import com.t13max.game.msg.Message;
import message.id.MessageId;

/**
 * @author: t13max
 * @since: 17:13 2024/5/30
 */
@Message(value = MessageId.S_MATCH_ACTION_VALUE)
public class DoActionRespMessage extends AbstractMessage<DoActionResp> {

    @Override
    public void doMessage(Player player, int msgId, DoActionResp message) {
        LogWindow logWindow = Player.PLAYER.getWindow("log");
        if (logWindow == null) {
            Player.PLAYER.openWindow("log", false);
        }

        logWindow.update(message);

        HomeWindow homeWindow = Player.PLAYER.getWindow("home");

        homeWindow.update(message);
    }
}
