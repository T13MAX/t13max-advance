package com.t13max.game.feature.active;

import com.t13max.data.entity.activity.ActivityData;
import com.t13max.game.memory.SingleMemory;
import game.entity.ActivityDataPb;

/**
 * @author: t13max
 * @since: 20:42 2024/6/4
 */
public class ActivityMemory extends SingleMemory<ActivityDataPb, ActivityData> {


    @Override
    public void init() {

    }

    @Override
    public Class<ActivityData> getDataClazz() {
        return ActivityData.class;
    }

    @Override
    public ActivityDataPb buildPb() {
        ActivityDataPb.Builder builder = ActivityDataPb.newBuilder();
        return builder.build();
    }


}
