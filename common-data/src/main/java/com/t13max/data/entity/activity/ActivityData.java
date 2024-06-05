package com.t13max.data.entity.activity;

import com.t13max.data.entity.IData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 20:43 2024/6/4
 */
public class ActivityData implements IData {

    //玩家id
    private long uuid;

    private Map<Integer, IActivityFeature> activityDataMap;

    @Override
    public long getId() {
        return uuid;
    }

    public ActivityData() {
        activityDataMap = new HashMap<>();
    }
}
