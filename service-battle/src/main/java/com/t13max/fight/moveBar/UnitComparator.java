package com.t13max.fight.moveBar;

import java.util.Comparator;

/**
 * @author: t13max
 * @since: 13:34 2024/4/11
 */
public class UnitComparator implements Comparator<MoveBarUnit> {

    @Override
    public int compare(MoveBarUnit o1, MoveBarUnit o2) {

        if (o2.getCurDistance() == o1.getCurDistance()) {
            if (o2.getSpeed() == o1.getSpeed()) {
                return (int) (o2.getHeroId() - o1.getHeroId());
            }
            return (int) (o2.getCurDistance() - o1.getCurDistance());
        }
        return (int) (o2.getCurDistance() - o1.getCurDistance());
    }
}
