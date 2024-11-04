package com.t13max.data.entity;

import com.t13max.data.mongo.IData;
import com.t13max.data.mongo.collection.XSet;
import lombok.Data;

/**
 * @author t13max
 * @since 16:37 2024/10/31
 */
@Data
public class RankKeyData implements IData {

    private long id;

    private XSet<String> rankKeySet;
}
