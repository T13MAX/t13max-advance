package com.t13max.fight.damage;

/**
 * @author: t13max
 * @since: 14:50 2024/5/24
 */
public interface CalcConst {

    public static final int MAX_RATE = 100;

    public static final int DEF_CHANGE_POINT = 60;

    public static final int DEF_INVERSE = DEF_CHANGE_POINT * DEF_CHANGE_POINT / MAX_RATE;

}
