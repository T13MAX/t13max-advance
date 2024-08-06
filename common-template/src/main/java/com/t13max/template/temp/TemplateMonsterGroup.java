package com.t13max.template.temp;

import java.util.*;
import com.t13max.template.ITemplate;
import com.alibaba.excel.annotation.ExcelProperty;
import com.t13max.template.converter.*;

/**
 * MonsterGroup.xlsx
 * 
 *
 * @author t13max-template
 *
 * 系统生成类 请勿修改
 */
public class TemplateMonsterGroup implements ITemplate {

    /** id */
    @ExcelProperty("id")
    public final int id;
    /** monsters */
    @ExcelProperty(value = "monsters", converter = ToIntListConverter.class)
    public final List<Integer> monsters;

    public TemplateMonsterGroup(int id, List<Integer> monsters) {
        this.id = id;
        this.monsters = monsters;
    }

    @Override
    public int getId() {
        return id;
    }
}