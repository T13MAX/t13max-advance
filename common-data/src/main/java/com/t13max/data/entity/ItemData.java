package com.t13max.data.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 道具 可堆叠 只有数量
 *
 * @author: t13max
 * @since: 18:39 2024/6/4
 */
@Data
public class ItemData implements IData {

    private long uuid;

    private Map<Integer, Long> itemMap;//templateId->num

    public ItemData() {
        this.itemMap = new HashMap<>();
    }

    public ItemData(long uuid) {
        this.uuid = uuid;
        this.itemMap = new HashMap<>();
    }

    /**
     * 用玩家id作为唯一id
     *
     * @Author t13max
     * @Date 19:24 2024/6/4
     */
    public long getId() {
        return uuid;
    }
}
