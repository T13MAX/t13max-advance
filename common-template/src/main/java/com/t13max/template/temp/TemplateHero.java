package com.t13max.template.temp;

import java.util.*;
import com.t13max.template.ITemplate;
import com.alibaba.excel.annotation.ExcelProperty;
import com.t13max.template.converter.*;

/**
 * hero.xlsx
 * 
 *
 * @author t13max-template
 *
 * 系统生成类 请勿修改
 */
public class TemplateHero implements ITemplate {

    /** id */
    @ExcelProperty("id")
    public final int id;
    /** name */
    @ExcelProperty("name")
    public final String name;
    /** atk */
    @ExcelProperty("atk")
    public final int atk;
    /** def */
    @ExcelProperty("def")
    public final int def;
    /** hp */
    @ExcelProperty("hp")
    public final int hp;
    /** speed */
    @ExcelProperty("speed")
    public final int speed;
    /** element */
    @ExcelProperty("element")
    public final int element;
    /** mastery */
    @ExcelProperty("mastery")
    public final int mastery;
    /** skill */
    @ExcelProperty(value = "skill", converter = ToIntListConverter.class)
    public final List<Integer> skill;
    /** des */
    @ExcelProperty("des")
    public final String des;

    public TemplateHero(int id, String name, int atk, int def, int hp, int speed, int element, int mastery, List<Integer> skill, String des) {
        this.id = id;
        this.name = name;
        this.atk = atk;
        this.def = def;
        this.hp = hp;
        this.speed = speed;
        this.element = element;
        this.mastery = mastery;
        this.skill = skill;
        this.des = des;
    }

    @Override
    public int getId() {
        return id;
    }
}