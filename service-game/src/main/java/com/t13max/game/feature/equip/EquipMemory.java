package com.t13max.game.feature.equip;

import com.t13max.data.entity.EquipData;
import com.t13max.game.memory.MapMemory;
import game.entity.EquipDataPb;


/**
 * @author: t13max
 * @since: 18:44 2024/6/4
 */
public class EquipMemory extends MapMemory<EquipDataPb, EquipData> {

    @Override
    public void init() {

    }

    @Override
    public EquipDataPb buildPb(long id) {
        EquipData equipData = dataMap.get(id);
        if (equipData == null) {
            return null;
        }
        EquipDataPb.Builder builder = EquipDataPb.newBuilder();
        builder.setId(equipData.getId());
        builder.setTemplateId(equipData.getTemplateId());
        return builder.build();
    }

    @Override
    public Class<EquipData> getDataClazz() {
        return EquipData.class;
    }
}
