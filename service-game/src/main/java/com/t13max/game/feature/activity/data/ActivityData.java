package com.t13max.game.feature.activity.data;

import com.t13max.data.entity.IData;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: t13max
 * @since: 20:43 2024/6/4
 */
@Getter
public class ActivityData implements IData {

    //玩家id
    private final long roleId;

    //玩家已完成活动列表
    private final List<Integer> finishedList;

    private final Map<Integer, IActFeature> activityDataMap;

    @Override
    public long getId() {
        return roleId;
    }

    public ActivityData(long roleId) {
        this.roleId = roleId;
        this.finishedList = new LinkedList<>();
        this.activityDataMap = new HashMap<>();
    }

    public ActivityData(long roleId, List<Integer> finishedList, Map<Integer, IActFeature> activityDataMap) {
        this.roleId = roleId;
        this.finishedList = finishedList;
        this.activityDataMap = activityDataMap;
    }

    public <T extends IActFeature> T getData(int actId) {
        return (T) activityDataMap.get(actId);
    }
}
