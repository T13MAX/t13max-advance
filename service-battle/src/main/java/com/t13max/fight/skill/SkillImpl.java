package com.t13max.fight.skill;

import com.t13max.template.temp.TemplateSkill;
import lombok.Data;

/**
 * @author: t13max
 * @since: 15:56 2024/4/15
 */
@Data
public class SkillImpl {

    private int skillId;

    private int collDown;

    public SkillImpl(TemplateSkill templateSkill) {
        this.skillId = templateSkill.getId();
    }

    /**
     * 技能消耗
     *
     * @Author t13max
     * @Date 16:07 2024/4/15
     */
    public void consumeCost() {

    }

    public void increaseCoolDown() {

    }

    public void decreaseCoolDown() {

    }
}
