package com.t13max.game.feature.active.data;

import game.entity.ActivityDataPb;

/**
 * @author: t13max
 * @since: 20:59 2024/6/4
 */
public interface IActFeature {

    int getActivityId();

    void setActivityId(int activityId);

    int getType();

    void setType(int type);

    ActivityDataPb toProto();
}
