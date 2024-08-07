package com.t13max.template.temp;

import java.util.*;
import com.t13max.template.ITemplate;

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
    public final int id;
    /** heroTempId */
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