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
public class TemplateBuff implements ITemplate{

    private int id;

    private String[] activeCondition;

    private String[] disposedCondition;

    private int[] effect;

    private String[] params;

}
