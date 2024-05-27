package com.t13max.fight.event;

/**
 * @author: t13max
 * @since: 15:15 2024/4/10
 */
public enum FightEventEnum {
    BEFORE_ACTION,
    DO_ACTION,
    AFTER_ACTION,

    SMALL_ROUND_BEGIN,
    SMALL_ROUND_END,
    FOOT_UP,

    BUFF_UPDATE,
    BUFF_SWITCH_TO_ACTIVE,

    BUFF_EFFECT_CAN_ACTIVE,
    BUFF_EFFECT_CAN_DISPOSED,

    UNIT_DEAD,
    ATTRIBUTE_UPDATE,
    READY_TO_ADD_HP,
    READY_TO_SUB_HP,
    ;

}
