package com.t13max.template.temp;

import java.util.*;
import com.t13max.template.ITemplate;
import com.alibaba.excel.annotation.ExcelProperty;
import com.t13max.template.converter.*;

/**
 * const.xlsx
 * 
 *
 * @author t13max-template
 *
 * 系统生成类 请勿修改
 */
public class TemplateConst implements ITemplate {

    /** id */
    @ExcelProperty("id")
    public final int id;
    /** params */
    @ExcelProperty("params")
    public final String params;
    /** des */
    @ExcelProperty("des")
    public final String des;

    public TemplateConst(int id, String params, String des) {
        this.id = id;
        this.params = params;
        this.des = des;
    }

    @Override
    public int getId() {
        return id;
    }
}