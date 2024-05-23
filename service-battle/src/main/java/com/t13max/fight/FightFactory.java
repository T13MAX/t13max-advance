package com.t13max.fight;

import com.t13max.fight.member.FightMember;
import com.t13max.fight.moveBar.ActionMoveBar;
import com.t13max.template.helper.BuffHelper;
import com.t13max.template.helper.HeroHelper;
import com.t13max.template.manager.TemplateManager;
import com.t13max.template.temp.TemplateHero;
import com.t13max.util.RandomUtil;
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

    public FightMatch createFightImpl() {
        FightMatch fight = new FightMatch();
        return fight;
    }

    public FightMatch quickCreateFightImpl() {
        FightMatch fight = null;
        try {

            fight = new FightMatch(UuidUtil.getNextId());

            Map<Long, FightHero> attacker = quickGenHero(createFightMember(UuidUtil.getNextId(), true), fight);
            Map<Long, FightHero> defender = quickGenHero(createFightMember(UuidUtil.getNextId(), false), fight);

            fight.setAttacker(attacker);
            fight.setDefender(defender);
            fight.setActionMoveBar(new ActionMoveBar(attacker, defender));

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return fight;
    }

    public FightMember createFightMember(long uid, boolean attacker) {
        return new FightMember(uid, attacker);
    }

    public Map<Long, FightHero> quickGenHero(FightMember fightMember, FightMatch fight) {
        Map<Long, FightHero> result = new HashMap<>();
        HeroHelper heroHelper = TemplateManager.inst().helper(HeroHelper.class);
        ArrayList<TemplateHero> templateHeroes = new ArrayList<>(heroHelper.getAll());
        for (int i = 0; i < 5; i++) {
            TemplateHero templateHero = RandomUtil.random(templateHeroes);
            FightHero fightHero = new FightHero(UuidUtil.getNextId(), fightMember, templateHero.getId(), fight);
            result.put(fightHero.getId(), fightHero);
        }
        return result;
    }
}
