package com.t13max.template.helper;

import com.t13max.template.ITemplate;
import com.t13max.template.temp.TemplateBuff;
import com.t13max.template.temp.TemplateSkill;

/**
 * @author: t13max
 * @since: 15:14 2024/5/23
 */
public class SkillHelper extends TemplateHelper<TemplateSkill>{

    public SkillHelper() {
        super("skill.json");
    }

    @Override
    public boolean configCheck() {
        return true;
    }

    @Override
    public Class<? extends ITemplate> getClazz() {
        return TemplateSkill.class;
    }
}
