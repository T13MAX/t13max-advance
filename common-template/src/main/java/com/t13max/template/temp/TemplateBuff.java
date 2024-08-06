package com.t13max.template.temp;

import java.util.*;
import com.t13max.template.ITemplate;
import com.alibaba.excel.annotation.ExcelProperty;
import com.t13max.template.converter.*;

/**
 * buff.xlsx
 * 
 *
 * @author t13max-template
 *
 * 系统生成类 请勿修改
 */
public class TemplateBuff implements ITemplate {

    /** id */
    @ExcelProperty("id")
    public final int id;
    /** disposedRule */
    @ExcelProperty("disposedRule")
    public final int disposedRule;
    /** activeCondition */
    @ExcelProperty(value = "activeCondition", converter = ToStrListConverter.class)
    public final List<String> activeCondition;
    /** disposedCondition */
    @ExcelProperty(value = "disposedCondition", converter = ToStrListConverter.class)
    public final List<String> disposedCondition;
    /** effect */
    @ExcelProperty(value = "effect", converter = ToIntListConverter.class)
    public final List<Integer> effect;
    /** params */
    @ExcelProperty(value = "params", converter = ToStrListConverter.class)
    public final List<String> params;
    /** des */
    @ExcelProperty("des")
    public final String des;

    public TemplateBuff(int id, int disposedRule, List<String> activeCondition, List<String> disposedCondition, List<Integer> effect, List<String> params, String des) {
        this.id = id;
        this.disposedRule = disposedRule;
        this.activeCondition = activeCondition;
        this.disposedCondition = disposedCondition;
        this.effect = effect;
        this.params = params;
        this.des = des;
    }

    @Override
    public int getId() {
        return id;
    }
}