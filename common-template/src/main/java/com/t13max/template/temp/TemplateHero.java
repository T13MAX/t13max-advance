package com.t13max.template.temp;

import java.util.*;
import com.t13max.template.ITemplate;

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
    public final int id;
    /** name */
    public final String name;
    /** atk */
    public final int atk;
    /** def */
    public final int def;
    /** hp */
    public final int hp;
    /** speed */
    public final int speed;
    /** element */
    public final int element;
    /** mastery */
    public final int mastery;
    /** skill */
    public final List<Integer> skill;
    /** des */
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
        this.skill = Collections.unmodifiableList(skill);
        this.des = des;
    }

    @Override
    public int getId() {
        return id;
    }
}