package com.t13max.template.temp;

import java.util.*;
import com.t13max.template.ITemplate;
import com.alibaba.excel.annotation.ExcelProperty;
import com.t13max.template.converter.*;

/**
 * monster.xlsx
 * 
 *
 * @author t13max-template
 *
 * 系统生成类 请勿修改
 */
public class TemplateMonster implements ITemplate {

    /** id */
    @ExcelProperty("id")
    public final int id;
    /** heroTempId */
    @ExcelProperty("heroTempId")
    public final int heroTempId;

    public TemplateMonster(int id, int heroTempId) {
        this.id = id;
        this.heroTempId = heroTempId;
    }

    @Override
    public int getId() {
        return id;
    }
}