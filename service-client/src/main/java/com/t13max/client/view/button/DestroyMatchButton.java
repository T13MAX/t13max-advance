package com.t13max.client.view.button;

import battle.api.CreateFightMatchReq;
import battle.api.DestroyFightMatchReq;
import battle.entity.FightHeroInfoPb;
import battle.entity.FightPlayerInfoPb;
import com.t13max.client.player.Player;
import com.t13max.client.player.task.SendMsgTask;
import com.t13max.util.TempIdUtil;
import message.id.MessageId;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author: t13max
 * @since: 17:11 2024/5/30
 */
public class DestroyMatchButton extends JButton {

    public DestroyMatchButton() {
        this.setText("销毁");
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Player.PLAYER.getMatchId() == 0) {
                    return;
                }
                DestroyFightMatchReq.Builder destroyFightMatchReq = DestroyFightMatchReq.newBuilder();
                destroyFightMatchReq.setMatchId(Player.PLAYER.getMatchId());
                new SendMsgTask(MessageId.C_DESTROY_MATCH_VALUE, destroyFightMatchReq.build()).submit();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

}
