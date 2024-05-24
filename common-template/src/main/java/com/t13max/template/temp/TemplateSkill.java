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
    private int[] toSelfImpacts;
    private int[] toOtherImpacts;
    private String[] selfParams;
    private String[] otherParams;
    private String des;
}

