package com.t13max.game.memory;

import com.google.protobuf.MessageLite;
import com.t13max.data.dao.MongoUtil;
import com.t13max.data.entity.IData;
import com.t13max.data.manager.AsyncSaveManager;


/**
 * 单个数据
 *
 * @author: t13max
 * @since: 19:13 2024/6/4
 */
public abstract class SingleMemory<PB extends MessageLite, DATA extends IData> extends AbstractMemory<PB, DATA> {

    protected DATA data;

    @Override
    public void load() {
        this.data = MongoUtil.findById(player.getUuid(), getDataClazz());
    }

    @Override
    public void batchSave() {
        AsyncSaveManager.inst().save(data);
    }

    public abstract PB buildPb();

    public DATA get() {
        return data;
    }
}
