package com.t13max.template.temp;

import java.util.*;
import com.t13max.template.ITemplate;

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
    public final int id;
    /** monsters */
    public final List<Integer> monsters;

    public TemplateMonsterGroup(int id, List<Integer> monsters) {
        this.id = id;
        this.monsters = Collections.unmodifiableList(monsters);
    }

    @Override
    public int getId() {
        return id;
    }
}