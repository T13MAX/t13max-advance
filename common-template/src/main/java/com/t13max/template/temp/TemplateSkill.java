package com.t13max.template.temp;

import java.util.*;
import com.t13max.template.ITemplate;
import com.alibaba.excel.annotation.ExcelProperty;
import com.t13max.template.converter.*;

/**
 * skill.xlsx
 * 
 *
 * @author t13max-template
 *
 * 系统生成类 请勿修改
 */
public class TemplateSkill implements ITemplate {

    /** id */
    @ExcelProperty("id")
    public final int id;
    /** type */
    @ExcelProperty("type")
    public final int type;
    /** toSelfImpacts */
    @ExcelProperty(value = "toSelfImpacts", converter = ToIntListConverter.class)
    public final List<Integer> toSelfImpacts;
    /** toOtherImpacts */
    @ExcelProperty(value = "toOtherImpacts", converter = ToIntListConverter.class)
    public final List<Integer> toOtherImpacts;
    /** coldDown */
    @ExcelProperty("coldDown")
    public final int coldDown;
    /** selfParams */
    @ExcelProperty(value = "selfParams", converter = ToStrListConverter.class)
    public final List<String> selfParams;
    /** otherParams */
    @ExcelProperty(value = "otherParams", converter = ToStrListConverter.class)
    public final List<String> otherParams;
    /** otherSelector */
    @ExcelProperty("otherSelector")
    public final int otherSelector;
    /** des */
    @ExcelProperty("des")
    public final String des;

    public TemplateSkill(int id, int type, List<Integer> toSelfImpacts, List<Integer> toOtherImpacts, int coldDown, List<String> selfParams, List<String> otherParams, int otherSelector, String des) {
        this.id = id;
        this.type = type;
        this.toSelfImpacts = toSelfImpacts;
        this.toOtherImpacts = toOtherImpacts;
        this.coldDown = coldDown;
        this.selfParams = selfParams;
        this.otherParams = otherParams;
        this.otherSelector = otherSelector;
        this.des = des;
    }

    @Override
    public int getId() {
        return id;
    }
}