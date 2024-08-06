package com.t13max.template.helper;

import com.t13max.template.ITemplate;
import com.t13max.template.temp.TemplateMonster;

/**
 * @author: t13max
 * @since: 15:14 2024/5/23
 */
public class MonsterHelper extends TemplateHelper<TemplateMonster>{

    public MonsterHelper() {
        super("TemplateMonster.json");
    }

    @Override
    public boolean configCheck() {
        return true;
    }

    @Override
    public Class<? extends ITemplate> getClazz() {
        return TemplateMonster.class;
    }

}
