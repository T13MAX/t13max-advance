package com.t13max.fight.skill;

import com.t13max.template.temp.TemplateSkill;

/**
 * @author: t13max
 * @since: 18:42 2024/5/27
 */
public interface IFightSkill {

    int getSkillId();

    TemplateSkill getTemplateSkill();

    //是否可用
    boolean available();

    //使用消耗
    boolean consumeCost();
}
