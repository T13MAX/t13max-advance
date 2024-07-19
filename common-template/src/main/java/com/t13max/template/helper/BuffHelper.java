package com.t13max.template.helper;

import com.t13max.game.util.Log;
import com.t13max.template.ITemplate;
import com.t13max.template.temp.TemplateBuff;

/**
 * @author: t13max
 * @since: 15:14 2024/5/23
 */
public class BuffHelper extends TemplateHelper<TemplateBuff> {

    public BuffHelper() {
        super("buff.json");
    }

    @Override
    public boolean configCheck() {
        for (TemplateBuff templateBuff : getTempAll()) {
            int length = templateBuff.getEffect().length;
            if (length != templateBuff.getParams().length) {
                Log.battle.error("配表错误, templateBuff.getParams().length, id={}", templateBuff.getId());
                return false;
            }
            if (length != templateBuff.getActiveCondition().length) {
                Log.battle.error("配表错误, templateBuff.getActiveCondition().length, id={}", templateBuff.getId());
                return false;
            }
            if (length != templateBuff.getDisposedCondition().length) {
                Log.battle.error("配表错误, templateBuff.getDisposedCondition().length, id={}", templateBuff.getId());
                return false;
            }
        }
        return true;
    }

    @Override
    public Class<? extends ITemplate> getClazz() {
        return TemplateBuff.class;
    }
}
