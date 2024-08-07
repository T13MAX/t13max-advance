package com.t13max.template.temp;

import java.util.*;
import com.t13max.template.ITemplate;

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
    public final int id;
    /** disposedRule */
    public final int disposedRule;
    /** activeCondition */
    public final List<String> activeCondition;
    /** disposedCondition */
    public final List<String> disposedCondition;
    /** effect */
    public final List<Integer> effect;
    /** params */
    public final List<String> params;
    /** des */
    public final String des;

    public TemplateBuff(int id, int disposedRule, List<String> activeCondition, List<String> disposedCondition, List<Integer> effect, List<String> params, String des) {
        this.id = id;
        this.disposedRule = disposedRule;
        this.activeCondition = Collections.unmodifiableList(activeCondition);
        this.disposedCondition = Collections.unmodifiableList(disposedCondition);
        this.effect = Collections.unmodifiableList(effect);
        this.params = Collections.unmodifiableList(params);
        this.des = des;
    }

    @Override
    public int getId() {
        return id;
    }
}