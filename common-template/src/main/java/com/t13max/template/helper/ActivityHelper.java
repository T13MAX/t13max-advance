package com.t13max.template.helper;

import com.t13max.template.ITemplate;
import com.t13max.template.temp.TemplateActivity;

/**
 * @author: t13max
 * @since: 15:14 2024/5/23
 */
public class ActivityHelper extends TemplateHelper<TemplateActivity> {

    public ActivityHelper() {
        super("TemplateActivity.json");
    }

    @Override
    public boolean configCheck() {
        for (TemplateActivity templateActivity : this.getAll()) {

        }
        return true;
    }

    @Override
    public Class<? extends ITemplate> getClazz() {
        return TemplateActivity.class;
    }
}
