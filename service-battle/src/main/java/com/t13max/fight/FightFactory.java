package com.t13max.fight;

import battle.api.CreateFightMatchReq;
import battle.entity.FightHeroInfoPb;
import battle.entity.FightPlayerInfoPb;
import com.t13max.fight.event.FightEventBus;
import com.t13max.fight.hero.FightHero;
import com.t13max.fight.log.FightLogManager;
import com.t13max.fight.member.IFightMember;
import com.t13max.fight.member.MonsterMember;
import com.t13max.fight.member.PlayerMember;
import com.t13max.fight.moveBar.ActionMoveBar;
import com.t13max.game.exception.BattleException;
import com.t13max.template.helper.HeroHelper;
import com.t13max.template.helper.MonsterGroupHelper;
import com.t13max.template.helper.MonsterHelper;
import com.t13max.template.manager.TemplateManager;
import com.t13max.template.temp.TemplateHero;
import com.t13max.template.temp.TemplateMonster;
import com.t13max.template.temp.TemplateMonsterGroup;
import com.t13max.util.Log;
import com.t13max.util.TempIdUtil;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 13:53 2024/4/11
 */
@UtilityClass
public class FightFactory {

    public static FightMatch createFightMatch(CreateFightMatchReq message) {

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

            IFightMember attackerMember = createPlayerMember(fightContext, attackerPb.getPlayerId(), true);

            fightMatch.getMemberMap().put(attackerMember.getId(), attackerMember);

            Map<Long, FightHero> attacker = createHeroMap(fightContext, attackerMember, attackerPb);
            Map<Long, FightHero> defender;
            IFightMember defenderMember;
            if (defenderPb.getMonsterGroupId() == 0) {
                defenderMember = createPlayerMember(fightContext, attackerPb.getPlayerId(), false);
                defender = createHeroMap(fightContext, defenderMember, defenderPb);
            } else {
                defenderMember = createMonsterMember(fightContext, TempIdUtil.getNextTempId(), false);
                defender = createHeroMap(fightContext, defenderMember, defenderPb.getMonsterGroupId());
            }
            fightMatch.getMemberMap().put(defenderMember.getId(), defenderMember);

            fightMatch.getHeroMap().putAll(attacker);
            fightMatch.getHeroMap().putAll(defender);

            fightMatch.setActionMoveBar(new ActionMoveBar(attacker, defender));

        } catch (Exception exception) {
            Log.battle.error("战斗创建失败, error={}", exception.getMessage());
            return null;
        }

        return fightMatch;
    }

    /**
     * 初始化战斗上下文 其他地方都从上下文获取 防止东西太多 注来注入的
     *
     * @Author t13max
     * @Date 15:42 2024/5/27
     */
    private static void initFightContext(FightContext fightContext) {
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
    public static IFightMember createPlayerMember(long uid) {
        return new PlayerMember(uid);
    }

    public static IFightMember createPlayerMember(FightContext fightContext, long uid, boolean attacker) {
        return new PlayerMember(fightContext, uid, attacker);
    }

    public static IFightMember createMonsterMember(FightContext fightContext, long uid, boolean attacker) {
        return new MonsterMember(fightContext, uid, attacker);
    }

    private static Map<Long, FightHero> createHeroMap(FightContext fightContext, IFightMember fightMember, int monsterGroupId) {
        MonsterGroupHelper monsterGroupHelper = TemplateManager.inst().helper(MonsterGroupHelper.class);
        TemplateMonsterGroup monsterGroup = monsterGroupHelper.getTemplate(monsterGroupId);
        if (monsterGroup == null) {
            throw new BattleException("TemplateMonsterGroup为空, monsterGroupId=" + monsterGroupId);
        }
        return createHeroMap(fightContext, fightMember, monsterGroup);
    }

    private static Map<Long, FightHero> createHeroMap(FightContext fightContext, IFightMember fightMember, TemplateMonsterGroup monsterGroup) {

        Map<Long, FightHero> result = new HashMap<>();
        MonsterHelper monsterHelper = TemplateManager.inst().helper(MonsterHelper.class);
        for (int monsterId : monsterGroup.getMonsters()) {
            TemplateMonster template = monsterHelper.getTemplate(monsterId);
            if (template == null) {
                throw new BattleException("TemplateMonster为空, monsterId=" + monsterId);
            }
            FightHero fightHero = createHero(fightContext, fightMember, TempIdUtil.getNextTempId(), template.getHeroTempId());
            fightHero.setAutoAction(true);
            result.put(fightHero.getId(), fightHero);
        }
        return result;
    }

    private static Map<Long, FightHero> createHeroMap(FightContext fightContext, IFightMember fightMember, FightPlayerInfoPb playerInfoPb) {
        Map<Long, FightHero> result = new HashMap<>();
        for (FightHeroInfoPb fightHeroInfoPb : playerInfoPb.getHeroListList()) {
            FightHero fightHero = createHero(fightContext, fightMember, fightHeroInfoPb.getHeroId(), fightHeroInfoPb.getTemplateId());
            result.put(fightHero.getId(), fightHero);
        }
        return result;
    }

    public static FightHero createHero(FightContext fightContext, IFightMember fightMember, long heroId, int template) {
        HeroHelper heroHelper = TemplateManager.inst().helper(HeroHelper.class);
        TemplateHero templateHero = heroHelper.getTemplate(template);
        return FightHero.createFightHero(fightContext, heroId, templateHero.getId(), fightMember);
    }

}
