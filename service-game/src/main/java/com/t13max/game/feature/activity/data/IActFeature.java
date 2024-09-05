package com.t13max.game.feature.activity.data;

import game.entity.ActivityDataPb;

/**
 * 一个具体的活动数据接口
 *
 * @author: t13max
 * @since: 20:59 2024/6/4
 */
public interface IActFeature {

    int getActivityId();

    int getType();

    ActivityDataPb toProto();
}
