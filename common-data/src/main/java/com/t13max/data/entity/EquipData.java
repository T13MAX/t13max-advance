package com.t13max.data.entity;

import com.t13max.data.mongo.IData;
import lombok.Data;

/**
 * 装备 不可堆叠 唯一
 * 需要控制最大数量
 *
 * @author: t13max
 * @since: 18:42 2024/6/4
 */
@Data
public class EquipData implements IData {

    private long id;

    private long uuid;

    private int templateId;

    //其他乱七八糟的数据
}
