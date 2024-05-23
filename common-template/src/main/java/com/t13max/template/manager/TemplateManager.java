package com.t13max.template.manager;


import com.t13max.template.temp.TemplateBuff;
import com.t13max.template.temp.TemplateHero;
import com.t13max.template.temp.TemplateSkill;

/**
 * @author: t13max
 * @since: 14:02 2024/4/11
 */
public class TemplateManager {

    public boolean load() {
        if (!TemplateBuff.load()) {
            return false;
        }
        if (!TemplateHero.load()) {
            return false;
        }
        if (!TemplateSkill.load()) {
            return false;
        }
        return true;
    }

    public void test() {
        TemplateBuff.getTemplate(120001);
        TemplateHero.getTemplate(100001);
        TemplateSkill.getTemplate(110001);
    }
}
