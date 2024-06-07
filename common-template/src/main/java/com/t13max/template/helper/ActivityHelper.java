package com.t13max.template.helper;

import com.t13max.template.ITemplate;
import com.t13max.template.temp.TemplateActivity;
import com.t13max.template.temp.TemplateBuff;
import com.t13max.util.Log;

/**
 * @author: t13max
 * @since: 15:14 2024/5/23
 */
public class ActivityHelper extends TemplateHelper<TemplateActivity> {

    public ActivityHelper() {
        super("activity.json");
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
