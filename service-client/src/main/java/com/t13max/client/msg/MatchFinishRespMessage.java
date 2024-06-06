package com.t13max.client.msg;

import battle.api.DestroyFightMatchResp;
import battle.api.FightMatchFinishPush;
import com.t13max.client.player.Player;
import com.t13max.client.view.window.AbstractWindow;
import com.t13max.client.view.window.HomeWindow;
import com.t13max.game.msg.Message;
import message.id.MessageId;

import javax.swing.*;

/**
 * @author: t13max
 * @since: 17:13 2024/5/30
 */
@Message(value = MessageId.S_MATCH_FINISH_PUSH_VALUE)
public class MatchFinishRespMessage extends AbstractMessage<FightMatchFinishPush> {


    @Override
    public void doMessage(Player player, int msgId, FightMatchFinishPush message) {
        if (message.getMatchId() == player.getMatchId()) {
            player.getMatchEntity().clear();
            HomeWindow home = player.getWindow("home");
            JOptionPane.showMessageDialog(home, "战斗结束！");
        }
    }
}
