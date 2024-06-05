package com.t13max.game.feature.item;

import com.t13max.data.entity.ItemData;
import com.t13max.game.memory.SingleMemory;
import game.entity.ItemDataPb;

/**
 * @author: t13max
 * @since: 18:38 2024/6/4
 */
public class ItemMemory extends SingleMemory<ItemDataPb, ItemData> {

    @Override
    public void init() {
        //初始化玩家道具
        data = new ItemData(player.getUuid());
        //走配表 初始化玩家初始道具
    }

    @Override
    public Class<ItemData> getDataClazz() {
        return ItemData.class;
    }

    @Override
    public ItemDataPb buildPb() {
        ItemDataPb.Builder builder = ItemDataPb.newBuilder();
        builder.putAllItemMap(data.getItemMap());
        return builder.build();
    }
}
