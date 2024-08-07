package com.t13max.template.temp;

import java.util.*;
import com.t13max.template.ITemplate;

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
    public final int id;
    /** type */
    public final int type;
    /** toSelfImpacts */
    public final List<Integer> toSelfImpacts;
    /** toOtherImpacts */
    public final List<Integer> toOtherImpacts;
    /** coldDown */
    public final int coldDown;
    /** selfParams */
    public final List<String> selfParams;
    /** otherParams */
    public final List<String> otherParams;
    /** otherSelector */
    public final int otherSelector;
    /** des */
    public final String des;

    public TemplateSkill(int id, int type, List<Integer> toSelfImpacts, List<Integer> toOtherImpacts, int coldDown, List<String> selfParams, List<String> otherParams, int otherSelector, String des) {
        this.id = id;
        this.type = type;
        this.toSelfImpacts = Collections.unmodifiableList(toSelfImpacts);
        this.toOtherImpacts = Collections.unmodifiableList(toOtherImpacts);
        this.coldDown = coldDown;
        this.selfParams = Collections.unmodifiableList(selfParams);
        this.otherParams = Collections.unmodifiableList(otherParams);
        this.otherSelector = otherSelector;
        this.des = des;
    }

    @Override
    public int getId() {
        return id;
    }
}