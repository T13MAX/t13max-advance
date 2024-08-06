package com.t13max.template.helper;

import com.t13max.template.ITemplate;
import com.t13max.template.temp.TemplateMonsterGroup;

/**
 * @author: t13max
 * @since: 15:14 2024/5/23
 */
public class MonsterGroupHelper extends TemplateHelper<TemplateMonsterGroup>{

    public MonsterGroupHelper() {
        super("TemplateMonsterGroup.json");
    }

    @Override
    public boolean configCheck() {
        return true;
    }

    @Override
    public Class<? extends ITemplate> getClazz() {
        return TemplateMonsterGroup.class;
    }

}
