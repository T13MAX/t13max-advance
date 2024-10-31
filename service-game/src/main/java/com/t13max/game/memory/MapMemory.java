package com.t13max.game.memory;

import com.google.protobuf.MessageLite;
import com.t13max.data.mongo.AutoSaveManager;
import com.t13max.data.mongo.IData;
import com.t13max.data.mongo.MongoManager;
import com.t13max.game.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * 多个数据 map存储
 *
 * @author: t13max
 * @since: 19:13 2024/6/4
 */
public abstract class MapMemory<PB extends MessageLite, DATA extends IData> extends AbstractMemory<PB, DATA> {

    protected Map<Long, DATA> dataMap = new HashMap<>();

    @Override
    public void load() {
        List<DATA> dataList = MongoManager.inst().findList(getDataClazz());
        for (DATA data : dataList) {
            dataMap.put(data.getId(), data);
        }
    }

    @Override
    public void batchSave() {
        AutoSaveManager.inst().batchSave(getAll());
    }

    public abstract PB buildPb(long id);

    public void delete(long id) {
        DATA remove = dataMap.remove(id);
        AutoSaveManager.inst().delete(remove);
    }

    public List<PB> buildPbList() {
        List<PB> result = new LinkedList<>();
        for (Long id : dataMap.keySet()) {
            PB pb = buildPb(id);
            if (pb == null) {
                Log.data.error("build pb error! id={}", id);
                continue;
            }
            result.add(pb);
        }
        return result;
    }

    public DATA get(long id) {
        return dataMap.get(id);
    }

    public List<DATA> getAll() {
        return dataMap.values().stream().toList();
    }

}
