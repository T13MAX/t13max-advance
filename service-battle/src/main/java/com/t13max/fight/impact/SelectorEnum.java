package com.t13max.fight.impact;

import com.t13max.fight.hero.FightHero;
import com.t13max.util.RandomUtil;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: t13max
 * @since: 14:59 2024/5/24
 */
@Getter
public enum SelectorEnum {

    //自己
    SELF(1) {
        @Override
        public List<Long> select(FightHero fightHero) {
            return Collections.singletonList(fightHero.getId());
        }
    },

    //对方某个人
    TARGET(2) {
        @Override
        public List<Long> select(FightHero fightHero) {
            List<FightHero> heroList = fightHero.getFightContext().getFightMatch().getTargetHeroList(fightHero.isAttacker());
            return Collections.singletonList(RandomUtil.random(heroList).getId());
        }
    },

    //友方多个
    SELF_MANY(3) {
        @Override
        public List<Long> select(FightHero fightHero) {
            List<FightHero> heroList = fightHero.getFightContext().getFightMatch().getTargetHeroList(fightHero.isAttacker());
            return RandomUtil.random(heroList, heroList.size()).stream().map(FightHero::getId).toList();
        }
    },

    //对方多个
    OTHER_MANY(4) {
        @Override
        public List<Long> select(FightHero fightHero) {
            List<FightHero> heroList = fightHero.getFightContext().getFightMatch().getTargetHeroList(!fightHero.isAttacker());
            return RandomUtil.random(heroList, heroList.size()).stream().map(FightHero::getId).toList();
        }
    },

    //后续增加复杂的选择器

    ;
    private static final Map<Integer, SelectorEnum> selectorEnumMap = new HashMap<>();

    static {
        for (SelectorEnum selectorEnum : values()) {
            selectorEnumMap.put(selectorEnum.getId(), selectorEnum);
        }
    }

    private int id;

    SelectorEnum(int id) {
        this.id = id;
    }

    public abstract List<Long> select(FightHero fightHero);

    public static SelectorEnum getSelectorEnum(int id) {
        return selectorEnumMap.get(id);
    }
}
