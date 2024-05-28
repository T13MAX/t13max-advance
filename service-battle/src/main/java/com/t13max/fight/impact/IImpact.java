package com.t13max.fight.impact;

import com.t13max.fight.trigger.ITrigger;

/**
 * @author: t13max
 * @since: 17:07 2024/4/15
 */
public interface IImpact extends ITrigger {

    long getGenerator();

    ImpactEnum getImpactEnum();

    //对应的技能
    int getSkillId();

    void onCreate();

    void onDestroy();

    //参数校验
    boolean paramCheck();
}
