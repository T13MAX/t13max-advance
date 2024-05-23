package com.t13max.template.helper;

import com.t13max.template.ITemplate;
import com.t13max.template.temp.TemplateBuff;
import com.t13max.template.temp.TemplateConst;
import lombok.Getter;

/**
 * @author: t13max
 * @since: 15:14 2024/5/23
 */
public class ConstHelper extends TemplateHelper<TemplateConst> {

    public ConstHelper() {
        super("const.json");
    }

    @Override
    public boolean configCheck() {
        return true;
    }

    @Override
    public Class<? extends ITemplate> getClazz() {
        return TemplateConst.class;
    }

    public int getInt(ConstEnum constEnum) {
        TemplateConst templateConst = DATA_MAP.get(constEnum.getId());
        if (templateConst == null) {
            return 0;
        }
        return Integer.parseInt(templateConst.getParams());
    }

    public int getInt(ConstEnum constEnum, int def) {
        TemplateConst templateConst = DATA_MAP.get(constEnum.getId());
        if (templateConst == null) {
            return def;
        }
        return Integer.parseInt(templateConst.getParams());
    }

    public enum ConstEnum {

        行动值(130001),


        ;
        @Getter
        private int id;

        ConstEnum(int id) {
            this.id = id;
        }
    }
}