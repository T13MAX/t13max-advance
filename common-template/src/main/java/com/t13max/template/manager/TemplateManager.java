package com.t13max.template.manager;


import com.t13max.game.exception.CommonException;
import com.t13max.game.manager.ManagerBase;
import com.t13max.template.temp.TemplateBuff;
import com.t13max.template.temp.TemplateHero;
import com.t13max.template.temp.TemplateSkill;

import java.util.List;

/**
 * reload成功后替换 否则撤销操作 后续优化
 *
 * @author: t13max
 * @since: 14:02 2024/4/11
 */
public class TemplateManager extends ManagerBase {

    @Override
    public void init() {

        if (!this.load()) {
            throw new CommonException("配表加载失败");
        }

        this.configCheck();
    }

    /**
     * 校验表
     *
     * @Author t13max
     * @Date 14:33 2024/5/23
     */
    private void configCheck() {

    }

    /**
     * 第一次加载表
     *
     * @Author t13max
     * @Date 14:36 2024/5/23
     */
    public boolean load() {
        return load(false);
    }

    /**
     * 加载表 入参是否重新加载
     *
     * @Author t13max
     * @Date 14:36 2024/5/23
     */
    public boolean load(boolean reload) {
        try {
            if (!TemplateBuff.load()) {
                return false;
            }
            if (!TemplateHero.load()) {
                return false;
            }
            if (!TemplateSkill.load()) {
                return false;
            }
        } catch (Exception e) {
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
