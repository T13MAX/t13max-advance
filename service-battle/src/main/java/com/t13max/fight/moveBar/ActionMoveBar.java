package com.t13max.fight.moveBar;

import com.t13max.fight.FightHero;
import com.t13max.template.helper.ConstHelper;
import com.t13max.template.helper.HeroHelper;
import com.t13max.template.manager.TemplateManager;
import com.t13max.template.temp.TemplateHero;
import lombok.extern.log4j.Log4j2;

import java.util.*;

/**
 * 行动条
 *
 * @author: t13max
 * @since: 11:56 2024/4/11
 */
@Log4j2
public class ActionMoveBar {

    private UnitComparator comparator = new UnitComparator();

    private Map<Long, MoveBarUnit> actionUnitMap;

    public ActionMoveBar(Map<Long, FightHero> attacker, Map<Long, FightHero> defender) {
        Map<Long, MoveBarUnit> actionUnitMap = new HashMap<>();
        ConstHelper constHelper = TemplateManager.inst().helper(ConstHelper.class);
        HeroHelper heroHelper = TemplateManager.inst().helper(HeroHelper.class);
        int actionDistance = constHelper.getInt(ConstHelper.ConstEnum.行动值, 100);
        for (FightHero fightHero : attacker.values()) {
            TemplateHero template = heroHelper.getTemplate(fightHero.getTemplateId());
            actionUnitMap.put(fightHero.getId(), new MoveBarUnit(fightHero.getId(), true, template.getSpeed(), 0, actionDistance));
        }
        for (FightHero fightHero : defender.values()) {
            TemplateHero template = heroHelper.getTemplate(fightHero.getTemplateId());
            actionUnitMap.put(fightHero.getId(), new MoveBarUnit(fightHero.getId(), false, template.getSpeed(), 0, actionDistance));
        }
        this.actionUnitMap = actionUnitMap;
    }

    public boolean changeUnitSpeed(long heroId, int newSpeed) {

        MoveBarUnit moveBarUnit = this.actionUnitMap.get(heroId);
        if (moveBarUnit == null) {
            log.error("ActionMoveBar, changeUnitSpeed, MoveBarUnit不存在, heroId={}", heroId);
            return false;
        }

        moveBarUnit.setSpeed(newSpeed);

        return true;
    }

    public boolean removeUnit(long heroId) {
        if (!this.actionUnitMap.containsKey(heroId)) {
            return false;
        }

        MoveBarUnit remove = actionUnitMap.remove(heroId);
        if (remove == null) {
            log.error("ActionMoveBar, removeUnit, 移出失败, heroId={}", heroId);
            return false;
        }
        return true;
    }

    public void addUnit(MoveBarUnit moveBarUnit) {
        this.actionUnitMap.put(moveBarUnit.getHeroId(), moveBarUnit);
    }

    public MoveBarUnit getUnit(long heroId) {
        return actionUnitMap.get(heroId);
    }

    public MoveBarUnit getFastestUnit() {

        if (this.actionUnitMap.isEmpty()) {
            log.error("ActionMoveBar, getFastestUnit, 没人");
            return null;
        }

        List<MoveBarUnit> sortList = new ArrayList<>(this.actionUnitMap.values());

        sortList.sort(comparator);

        return sortList.get(0);
    }

    public void roll() {
        boolean hasWinner = false;

        for (int round = 0; round < 10000; round++) {

            for (MoveBarUnit moveBarUnit : this.actionUnitMap.values()) {
                float prevDistance = moveBarUnit.getCurDistance();
                moveBarUnit.setCurDistance(prevDistance + moveBarUnit.getSpeed());

                if (moveBarUnit.getCurDistance() >= moveBarUnit.getActionDistance()) {
                    hasWinner = true;
                }
            }

            if (hasWinner) {
                break;
            }

        }
    }
}
