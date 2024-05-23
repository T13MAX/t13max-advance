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
public class TemplateSkill implements ITemplate {

    private int id;
    private int type;
    private String toSelfImpacts;
    private String toOtherImpacts;
    private String params;
    private String des;

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

