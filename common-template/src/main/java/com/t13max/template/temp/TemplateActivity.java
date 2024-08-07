package com.t13max.template.temp;

import java.util.*;
import com.t13max.template.ITemplate;

/**
 * activity.xlsx
 * 
 *
 * @author t13max-template
 *
 * 系统生成类 请勿修改
 */
public class TemplateActivity implements ITemplate {

    /** id */
    public final int id;
    /** type */
    public final int type;
    /** openType */
    public final int openType;
    /** startTime */
    public final String startTime;
    /** endTime */
    public final String endTime;
    /** param */
    public final String param;

    public TemplateActivity(int id, int type, int openType, String startTime, String endTime, String param) {
        this.id = id;
        this.type = type;
        this.openType = openType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.param = param;
    }

    @Override
    public int getId() {
        return id;
    }
}