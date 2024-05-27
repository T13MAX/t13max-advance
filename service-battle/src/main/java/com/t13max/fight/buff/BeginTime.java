package com.t13max.fight.buff;

/**
 * 条件开始判断生效时机
 *
 * @author: t13max
 * @since: 13:30 2024/5/27
 */
public enum BeginTime {

    // 当前回合立即开始
    CURRENT_ROUND_BEGIN(0),

    // 下个回合开始
    NEXT_ROUND_BEGIN(1)

    ;

    private int type;

    BeginTime(int type) {
        this.type = type;
    }

}
