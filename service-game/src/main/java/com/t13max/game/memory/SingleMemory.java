package com.t13max.game.memory;

import com.google.protobuf.MessageLite;
import com.t13max.data.mongo.AutoSaveManager;
import com.t13max.data.mongo.IData;
import com.t13max.data.mongo.MongoManager;


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
        this.data = MongoManager.inst().findById(getDataClazz(), player.getUuid());
    }

    @Override
    public void batchSave() {
        AutoSaveManager.inst().save(data);
    }

    public abstract PB buildPb();

    public DATA get() {
        return data;
    }
}
