package com.t13max.fight;

import battle.api.CreateFightMatchReq;
import battle.entity.FightHeroInfoPb;
import battle.entity.FightPlayerInfoPb;
import com.t13max.fight.buff.BuffBoxImpl;
import com.t13max.fight.buff.BuffFactory;
import com.t13max.fight.context.FightFactory;
import com.t13max.fight.context.FightMatch;
import com.t13max.fight.context.MatchManager;
import com.t13max.fight.hero.FightHero;
import com.t13max.game.config.BattleConfig;
import com.t13max.game.run.Application;
import com.t13max.game.run.ApplicationConfig;
import com.t13max.game.util.Log;
import com.t13max.util.TempIdUtil;
import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

/**
 * 快速测试类
 *
 * @author: t13max
 * @since: 14:01 2024/5/23
 */
@ApplicationConfig(configClazz = BattleConfig.class)
public class BattleQuickTest {

    @Test
    public void test() throws Exception{

        String[] args = new String[]{};

        Application.run(BattleQuickTest.class, args);

        //快速测试
        quickTest();

        Log.scene.info("BattleQuickTest run!");

        LockSupport.park();
    }

    private void quickTest() {
        CreateFightMatchReq.Builder messageBuilder = CreateFightMatchReq.newBuilder();
        messageBuilder.setMatchId(TempIdUtil.getNextTempId());
        messageBuilder.setAttacker(quickCreateSpecialPlayer());
        messageBuilder.setDefender(FightPlayerInfoPb.newBuilder().setMonsterGroupId(150001));

        FightMatch fightMatch = FightFactory.createFightMatch(messageBuilder.build());

        if (fightMatch == null) {
            Log.battle.error("战斗创建失败");
            return;
        }

        for (FightHero fightHero : fightMatch.getHeroMap().values()) {
            if (!fightHero.isAttacker()) {
                continue;
            }
            //给自己挂一个牛逼逼的buff
            BuffBoxImpl buffBoxImpl = BuffFactory.createBuffBoxImpl(fightHero.getFightContext(), fightHero.getId(), 120000);
            fightHero.getBuffManager().addBuff(buffBoxImpl);
        }

        MatchManager.inst().addFightMatch(fightMatch);
    }


    private FightPlayerInfoPb quickCreateSpecialPlayer() {
        FightPlayerInfoPb.Builder playerInfoPb = FightPlayerInfoPb.newBuilder();
        playerInfoPb.setPlayerId(TempIdUtil.getNextTempId());
        FightHeroInfoPb.Builder builder = FightHeroInfoPb.newBuilder();
        builder.setHeroId(TempIdUtil.getNextTempId());
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
