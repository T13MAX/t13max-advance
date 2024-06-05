package com.t13max.fight.impact;

import com.t13max.fight.context.FightContext;
import com.t13max.fight.action.AbstractAction;
import com.t13max.fight.action.IAction;
import com.t13max.game.exception.BattleException;
import com.t13max.util.Log;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @author: t13max
 * @since: 17:45 2024/4/15
 */
@Log4j2
public class ImpactFactory {

    public static IImpact createImpact(FightContext fightContext, long generatorId, int skillId, String param, int impactId, boolean attacker, int delayTime, int generateRound, List<Long> targetIds) {
        ImpactEnum impactEnum = ImpactEnum.getImpact(impactId);
        if (impactEnum == null) {
            Log.battle.error("impactEnum不存在, impactId={}", impactId);
            return null;
        }

        AbstractAction result = null;
        try {
            Constructor<? extends IAction> constructor = impactEnum.getClazz().getConstructor();
            result = (AbstractAction) constructor.newInstance();
        } catch (Exception exception) {
            Log.battle.error("action创建出错, e={}", exception.getMessage());
            exception.printStackTrace();
            return null;
        }

        result.setFightContext(fightContext);
        result.setTargetHeroIds(targetIds);
        result.setGenerator(generatorId);
        result.setImpactEnum(impactEnum);
        result.setSkillId(skillId);
        result.setParam(param);
        result.setAttacker(attacker);
        result.setDelayTime(delayTime);
        result.setGenerateRound(generateRound);

        try {

            result.onCreate();

            if (!result.paramCheck()) return null;

        } catch (BattleException exception) {
            return null;
        }

        return result;
    }


}
