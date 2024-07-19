package com.t13max.fight.skill;

import com.t13max.fight.context.FightContext;
import com.t13max.game.util.Log;
import com.t13max.template.temp.TemplateSkill;
import lombok.experimental.UtilityClass;

/**
 * @author: t13max
 * @since: 18:42 2024/5/27
 */
@UtilityClass
public class SkillFactory {

    public static IFightSkill createFightSkill(FightContext fightContext, long ownerId, TemplateSkill templateSkill) {
        switch (templateSkill.getType()) {
            case 0 -> {
                return createFightNormalSkill(fightContext, ownerId, templateSkill);
            }
            case 1 -> {
                return createFightUltimateSkill(fightContext, ownerId, templateSkill);
            }
            case 2->{
                return createFightPassiveSkill(fightContext,ownerId,templateSkill);
            }
            default -> {
                Log.battle.error("未知技能类型. type={}", templateSkill.getType());
                return null;
            }
        }
    }

    private static IFightSkill createFightPassiveSkill(FightContext fightContext, long ownerId, TemplateSkill templateSkill) {
        FightPassiveSkill fightPassiveSkill = new FightPassiveSkill(fightContext, ownerId, templateSkill);
        return fightPassiveSkill;
    }

    private static IFightSkill createFightUltimateSkill(FightContext fightContext, long ownerId, TemplateSkill templateSkill) {
        FightNormalSkill fightNormalSkill = new FightNormalSkill(fightContext, ownerId, templateSkill);
        return fightNormalSkill;
    }

    private static IFightSkill createFightNormalSkill(FightContext fightContext, long ownerId, TemplateSkill templateSkill) {
        FightUltimateSkill fightUltimateSkill = new FightUltimateSkill(fightContext, ownerId, templateSkill);
        return fightUltimateSkill;
    }


}
