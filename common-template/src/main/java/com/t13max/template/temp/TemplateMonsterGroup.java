package com.t13max.template.temp;

import com.t13max.template.ITemplate;
import lombok.Data;

/**
 * @author: t13max
 * @since: 16:07 2024/5/28
 */
@Data
public class TemplateMonsterGroup implements ITemplate {

    private int id;

    private int[] monsters;

}
