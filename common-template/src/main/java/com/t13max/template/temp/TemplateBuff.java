package com.t13max.template.temp;

import com.t13max.template.ITemplate;
import com.t13max.template.util.JsonUtils;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: t13max
 * @since: 14:02 2024/4/11
 */
@Data
public class TemplateBuff implements ITemplate<TemplateBuff> {

    private final static String FILE_NAME = "buff.json";
    private final static int PRE = 12;

    private static final Map<Integer, TemplateBuff> DATA_MAP = new HashMap<>();

    private int id;

    private String activeCondition;

    private String disposedCondition;

    private int effect;

    private String params;

    public static boolean load() {
        List<TemplateBuff> iTemplates = JsonUtils.readCommodityTxt(FILE_NAME, TemplateBuff.class);
        if (iTemplates == null || iTemplates.isEmpty()) {
            return false;
        }
        iTemplates.forEach(e -> DATA_MAP.put(e.getId(), e));
        return true;
    }

    public static TemplateBuff getTemplate(int id) {
        return DATA_MAP.get(id);
    }
}
