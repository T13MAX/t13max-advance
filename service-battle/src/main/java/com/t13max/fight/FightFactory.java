package com.t13max.fight;

import battle.api.CreateFightMatchReq;
import battle.entity.FightHeroInfoPb;
import battle.entity.FightPlayerInfoPb;
import com.t13max.fight.enums.FightEnum;
import com.t13max.fight.enums.SmallRoundEnum;
import com.t13max.fight.event.FightEventBus;
import com.t13max.fight.log.FightLogManager;
import com.t13max.fight.member.FightMember;
import com.t13max.fight.moveBar.ActionMoveBar;
import com.t13max.template.helper.BuffHelper;
import com.t13max.template.helper.HeroHelper;
import com.t13max.template.manager.TemplateManager;
import com.t13max.template.temp.TemplateHero;
import com.t13max.util.RandomUtil;
import com.t13max.util.TimeUtil;
import com.t13max.util.UuidUtil;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 13:53 2024/4/11
 */
@UtilityClass
public class FightFactory {

    public FightMatch createFightMatch(CreateFightMatchReq message) {

        long matchId = message.getMatchId();
        FightPlayerInfoPb attackerPb = message.getAttacker();
        FightPlayerInfoPb defenderPb = message.getDefender();
        FightMatch fightMatch = null;
        try {

            fightMatch = new FightMatch(matchId);

            //创建战斗上下文
            FightContext fightContext = new FightContext();
            fightMatch.setFightContext(fightContext);

            initFightContext(fightContext);

            //战斗上下文初始化后
            fightContext.setFightMatch(fightMatch);
            fightContext.getFightEventBus().register(fightContext.getFightLogManager());

            Map<Long, FightHero> attacker = createHeroMap(fightContext, createFightMember(attackerPb.getPlayerId(), true), attackerPb);
            Map<Long, FightHero> defender = createHeroMap(fightContext, createFightMember(defenderPb.getPlayerId(), false), defenderPb);

            fightMatch.getHeroMap().putAll(attacker);
            fightMatch.getHeroMap().putAll(defender);

            fightMatch.setActionMoveBar(new ActionMoveBar(attacker, defender));

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return fightMatch;
    }

    /**
     * 初始化战斗上下文 其他地方都从上下文获取 防止东西太多 注来注入的
     *
     * @Author t13max
     * @Date 15:42 2024/5/27
     */
    private void initFightContext(FightContext fightContext) {
        fightContext.setFightEventBus(new FightEventBus(fightContext));
        fightContext.setFightTimeMachine(new FightTimeMachine(fightContext));
        fightContext.setFightLogManager(new FightLogManager(fightContext));
    }

    /**
     * 创建Member信息
     *
     * @Author t13max
     * @Date 16:11 2024/5/27
     */
    public FightMember createFightMember(long uid, boolean attacker) {
        return new FightMember(uid, attacker);
    }

    private static Map<Long, FightHero> createHeroMap(FightContext fightContext, FightMember fightMember, FightPlayerInfoPb playerInfoPb) {
        Map<Long, FightHero> result = new HashMap<>();
        for (FightHeroInfoPb fightHeroInfoPb : playerInfoPb.getHeroListList()) {
            FightHero fightHero = createHero(fightContext, fightMember, fightHeroInfoPb.getHeroId(), fightHeroInfoPb.getTemplateId());
            result.put(fightHero.getId(), fightHero);
        }
        return result;
    }

    public FightHero createHero(FightContext fightContext, FightMember fightMember, long heroId, int template) {
        HeroHelper heroHelper = TemplateManager.inst().helper(HeroHelper.class);
        TemplateHero templateHero = heroHelper.getTemplate(template);
        FightHero fightHero = FightHero.createFightHero(fightContext, heroId, templateHero.getId(), fightMember);
        return fightHero;
    }

}
