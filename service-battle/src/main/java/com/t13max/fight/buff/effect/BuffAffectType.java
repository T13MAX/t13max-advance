package com.t13max.fight.buff.effect;

/**
 * @author: t13max
 * @since: 11:32 2024/5/27
 */
public enum BuffAffectType {

    // 跳过出手
    SKIP_ACTION,

    // 只允许物理攻击
    ONLY_PHYSICAL_ATTACK,

    // 不受伤害
    IGNORE_ALL_DAMAGE,

    // 混乱, 使用普攻随机攻击一个单位
    CONFUSE,

    // 嘲讽, 只能普攻攻击给自己施放嘲讽的单位
    SPOOF,

    // 直接伤害计算强制按真实伤害
    ENFORCE_REAL_DAMAGE,

    // 暴击率影响，必暴击
    CRITICAL_MUST_BE,

    // 暴击率影响，必不暴击
    CRITICAL_MUST_NOT_BE,

    // 不死不灭(保1血)
    IMMORTAL,

    // 拉条免疫
    IGNORE_MOVE_BAR_FORWARD,

    // 退条免疫
    IGNORE_MOVE_BAR_BACKWARD,

}
