package com.t13max.template.helper;

import com.t13max.template.ITemplate;
import com.t13max.template.temp.TemplateBuff;
import com.t13max.template.temp.TemplateHero;

import java.util.Collection;

/**
 * @author: t13max
 * @since: 15:14 2024/5/23
 */
public class HeroHelper extends TemplateHelper<TemplateHero>{

    public HeroHelper() {
        super("hero.json");
    }

    @Override
    public boolean configCheck() {
        return true;
    }

    @Override
    public Class<? extends ITemplate> getClazz() {
        return TemplateHero.class;
    }

}
