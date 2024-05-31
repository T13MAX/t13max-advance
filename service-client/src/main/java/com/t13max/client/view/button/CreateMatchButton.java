package com.t13max.client.view.button;

import battle.api.CreateFightMatchReq;
import battle.entity.FightHeroInfoPb;
import battle.entity.FightPlayerInfoPb;
import com.t13max.client.player.Player;
import com.t13max.client.player.task.SendMsgTask;
import com.t13max.client.msg.CreateMatchRespMessage;
import com.t13max.util.TempIdUtil;
import message.id.MessageId;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author: t13max
 * @since: 17:11 2024/5/30
 */
public class CreateMatchButton extends JButton {

    public CreateMatchButton() {
        this.setText("创建");
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (Player.PLAYER.getMatchId() != 0) {
                    return;
                }
                CreateFightMatchReq createFightMatchReq = createCreateFightMatchReq();
                Player.PLAYER.setMatchId(createFightMatchReq.getMatchId());
                new SendMsgTask(MessageId.C_CREATE_MATCH_VALUE, createFightMatchReq).submit();
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

    private CreateFightMatchReq createCreateFightMatchReq() {
        CreateFightMatchReq.Builder messageBuilder = CreateFightMatchReq.newBuilder();
        messageBuilder.setMatchId(TempIdUtil.getNextTempId() + 10000000);
        messageBuilder.setAttacker(quickCreateSpecialPlayer());
        messageBuilder.setDefender(FightPlayerInfoPb.newBuilder().setMonsterGroupId(150001));
        return messageBuilder.build();
    }


    private FightPlayerInfoPb quickCreateSpecialPlayer() {
        FightPlayerInfoPb.Builder playerInfoPb = FightPlayerInfoPb.newBuilder();
        playerInfoPb.setPlayerId(Player.PLAYER.getUuid());
        FightHeroInfoPb.Builder builder = FightHeroInfoPb.newBuilder();
        builder.setHeroId(TempIdUtil.getNextTempId() + 10000000);
        builder.setTemplateId(100001);
        playerInfoPb.addHeroList(builder);
        return playerInfoPb.build();
    }

    private FightPlayerInfoPb quickCreatePlayer() {
        FightPlayerInfoPb.Builder playerInfoPb = FightPlayerInfoPb.newBuilder();
        playerInfoPb.setPlayerId(TempIdUtil.getNextTempId());
        for (int i = 0; i < 5; i++) {
            FightHeroInfoPb.Builder builder = FightHeroInfoPb.newBuilder();
            builder.setHeroId(TempIdUtil.getNextTempId());
            builder.setTemplateId(100000);
            playerInfoPb.addHeroList(builder);
        }
        return playerInfoPb.build();
    }
}
