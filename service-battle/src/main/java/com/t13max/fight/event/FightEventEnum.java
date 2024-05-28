package com.t13max.fight.event;

/**
 * @author: t13max
 * @since: 15:15 2024/4/10
 */
public enum FightEventEnum {

    //行动前
    BEFORE_ACTION,
    //行动
    DO_ACTION,
    //行动后
    AFTER_ACTION,

    //小回合开始
    SMALL_ROUND_BEGIN,
    //小回合结束
    SMALL_ROUND_END,
    //结算
    FOOT_UP,

    // 添加某Buff到宿主
    BUFF_ADD_TO_HOST,
    // 某Buff从宿主移除
    BUFF_REMOVE_FROM_HOST,
    // 某Buff更新
    BUFF_UPDATE,
    // 某Buff激活生效
    BUFF_SWITCH_TO_ACTIVE,
    //buff效果可以生效
    BUFF_EFFECT_CAN_ACTIVE,
    //buff效果消亡
    BUFF_EFFECT_CAN_DISPOSED,

    //单位死亡
    UNIT_DEAD,
    //属性变更
    ATTRIBUTE_UPDATE,
    //准备加血
    READY_TO_ADD_HP,
    //准备减血
    READY_TO_SUB_HP,
    ;

}
