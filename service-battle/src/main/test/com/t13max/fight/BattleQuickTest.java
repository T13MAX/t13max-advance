package com.t13max.fight;

import battle.api.CreateFightMatchReq;
import battle.entity.FightHeroInfoPb;
import battle.entity.FightPlayerInfoPb;
import com.t13max.game.config.BattleConfig;
import com.t13max.game.run.Application;
import com.t13max.game.run.ServerConfig;
import com.t13max.util.Log;
import com.t13max.util.UuidUtil;
import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

/**
 * 快速测试类
 *
 * @author: t13max
 * @since: 14:01 2024/5/23
 */
@ServerConfig(configClazz = BattleConfig.class)
public class BattleQuickTest {

    @Test
    public void test() {

        String[] args = new String[]{};

        Application.run(BattleQuickTest.class, args);

        //快速测试
        quickTest();

        Log.scene.info("BattleQuickTest run!");

        LockSupport.park();
    }

    private void quickTest() {
        CreateFightMatchReq.Builder messageBuilder = CreateFightMatchReq.newBuilder();
        messageBuilder.setMatchId(UuidUtil.getNextId());
        messageBuilder.setAttacker(quickCreatePlayer());
        messageBuilder.setDefender(quickCreatePlayer());

        FightMatch fightMatch = FightFactory.createFightMatch(messageBuilder.build());
        MatchManager.inst().addFightMatch(fightMatch);
    }


    private FightPlayerInfoPb quickCreatePlayer() {
        FightPlayerInfoPb.Builder playerInfoPb = FightPlayerInfoPb.newBuilder();
        playerInfoPb.setPlayerId(UuidUtil.getNextId());
        for (int i = 0; i < 5; i++) {
            FightHeroInfoPb.Builder builder = FightHeroInfoPb.newBuilder();
            playerInfoPb.addHeroList(builder);
        }
        return playerInfoPb.build();
    }
}
