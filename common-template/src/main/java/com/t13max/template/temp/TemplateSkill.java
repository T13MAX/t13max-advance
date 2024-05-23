package com.t13max.template.temp;

import com.t13max.template.ITemplate;
import com.t13max.template.util.JsonUtils;
import com.t13max.template.util.ParseUtil;
import lombok.Data;

import java.util.*;

/**
 * @author: t13max
 * @since: 14:02 2024/4/11
 */
@Data
public class TemplateSkill implements ITemplate<TemplateSkill> {

    private final static String FILE_NAME = "skill.json";

    private static final Map<Integer, TemplateSkill> DATA_MAP = new HashMap<>();

    private int id;
    private int type;
    private String toSelfImpacts;
    private String toOtherImpacts;
    private String params;
    private String des;


    public static boolean load() {

        List<TemplateSkill> iTemplates = JsonUtils.readCommodityTxt(FILE_NAME, TemplateSkill.class);
        if (iTemplates == null || iTemplates.isEmpty()) {
            return false;
        }
        iTemplates.forEach(e -> DATA_MAP.put(e.getId(), e));
        return true;
    }

    public static TemplateSkill getTemplate(int id) {
        return DATA_MAP.get(id);
    }

    public List<Integer> getToSelfImpacts() {
        if (toSelfImpacts == null || toSelfImpacts.isEmpty()) {
            return Collections.emptyList();
        }
        return ParseUtil.getIntList(this.toSelfImpacts, ";");
    }


    public List<Integer> getToOtherImpacts() {
        if (toOtherImpacts == null || toOtherImpacts.isEmpty()) {
            return Collections.emptyList();
        }
        return ParseUtil.getIntList(this.toOtherImpacts, ";");
    }

    public List<Integer> getIntListParam() {
        return ParseUtil.getIntList(this.params, ",");
    }
}

