package com.t13max.template;

import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: t13max
 * @since: 14:02 2024/4/11
 */
@Data
public class TemplateConst implements ITemplate<TemplateConst> {

    private final static String FILE_NAME = "const.json";
    private final static int PRE = 13;

    private static final Map<Integer, TemplateConst> DATA_MAP = new HashMap<>();

    private int id;

    private String params;

    public static boolean load() {
        List<TemplateConst> iTemplates = JsonUtils.readCommodityTxt(FILE_NAME, TemplateConst.class);
        if (iTemplates == null || iTemplates.isEmpty()) {
            return false;
        }
        iTemplates.forEach(e -> DATA_MAP.put(e.getId(), e));
        return true;
    }

    public static TemplateConst getTemplate(int id) {
        return DATA_MAP.get(id);
    }

    public static int getInt(ConstEnum constEnum) {
        TemplateConst templateConst = DATA_MAP.get(constEnum.getId());
        if (templateConst == null) {
            return 0;
        }
        return Integer.parseInt(templateConst.getParams());
    }

    public static int getInt(ConstEnum constEnum, int def) {
        TemplateConst templateConst = DATA_MAP.get(constEnum.getId());
        if (templateConst == null) {
            return def;
        }
        return Integer.parseInt(templateConst.getParams());
    }

    public static enum ConstEnum {

        行动值(130001),


        ;
        @Getter
        private int id;

        ConstEnum(int id) {
            this.id = id;
        }
    }
}
