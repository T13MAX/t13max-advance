package com.t13max.fight.enums;

/**
 * 小回合状态枚举
 *
 * @author: t13max
 * @since: 14:56 2024/4/10
 */
public enum SmallRoundEnum {

    //判断下一个行动的单位
    DETERMINE_NEXT_ACTION_UNIT,

    //小回合开始
    SMALL_ROUND_BEGIN,

    //等待行动
    WAIT_MANUAL,

    //行动
    DO_ACTION,

    //额外行动
    EXTRA_ACTION,

    //检查额外行动
    CHECK_EXTRA_ACTION,

    //跳过回合
    SKIP_ACTION,

    //行动结束
    ACTION_UNIT_END,

    //小回合结束
    SMALL_ROUND_END,

    //判断
    JUDGE,
}
