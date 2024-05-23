package com.t13max.template.helper;

import com.t13max.template.ITemplate;
import com.t13max.template.temp.TemplateBuff;

/**
 * @author: t13max
 * @since: 15:14 2024/5/23
 */
public class BuffHelper extends TemplateHelper<TemplateBuff>{

    public BuffHelper() {
        super("buff.json");
    }

    @Override
    public boolean configCheck() {
        return true;
    }

    @Override
    public Class<? extends ITemplate> getClazz() {
        return TemplateBuff.class;
    }
}
