package com.t13max.fight.attr;

/**
 * 属性变化原因
 *
 * @author: t13max
 * @since: 17:01 2024/5/27
 */
public enum AttrUpdateReason {
    //默认
    DEF,
    //反伤
    COUNTER_WOUND,
    //自己回血
    SELF_CURE,
    ;
}
