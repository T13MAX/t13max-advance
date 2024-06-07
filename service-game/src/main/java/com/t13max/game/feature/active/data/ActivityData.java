package com.t13max.game.feature.active.data;

import com.t13max.data.entity.IData;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 20:43 2024/6/4
 */
@Data
public class ActivityData implements IData {

    //玩家id
    private long uuid;

    private Map<Integer, IActFeature> activityDataMap;

    @Override
    public long getId() {
        return uuid;
    }

    public ActivityData() {
        activityDataMap = new HashMap<>();
    }

    public ActivityData(long uuid) {
        this.uuid = uuid;
        activityDataMap = new HashMap<>();
    }

    public <T extends IActFeature> T getData(int actId) {
        return (T)activityDataMap.get(actId);
    }
}
