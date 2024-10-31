package com.t13max.data.entity;

import com.t13max.data.mongo.IData;
import lombok.Data;

/**
 * 排行榜dump数据
 *
 * @author t13max
 * @since 17:39 2024/9/4
 */
@Data
public class RankDumpData implements IData {

    private long id;

    private String rankKey;

    private byte[] scoredSet;

    private byte[] mapperMap;

    private byte[] rankData;
}
