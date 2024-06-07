package com.t13max.template.temp;

import com.t13max.template.ITemplate;
import lombok.Data;

/**
 * @author: t13max
 * @since: 15:16 2024/6/6
 */
@Data
public class TemplateActivity implements ITemplate {
    //活动id
    private int id;
    //活动类型
    private int type;
    //开启类型 1~7 每周一~每周日开启 8 解析日期 x 后续添加(根据开服时间 合服时间 之类的
    private int openType;
    //开始时间 根据openType去解析
    private String startTime;
    //结束时间 同上
    private String endTime;
    //参数列表 灵活解析
    private String[] param;

}
