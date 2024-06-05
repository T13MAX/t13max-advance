package com.t13max.game.feature.active.data;

import com.t13max.data.entity.activity.IActivityFeature;
import com.t13max.util.TimeUtil;
import lombok.Data;

/**
 * @author: t13max
 * @since: 20:53 2024/6/4
 */
@Data
public class AbstractActivityData implements IActivityFeature {
    //活动id
    private int activityId;

    //活动类型
    private int type;

    //开启时间
    private long startTime;

    //结束时间
    private long endTime;

    //活动结束标记
    private boolean finish;

    //活动关闭标记
    private boolean close;

    //活动创建时间
    private long createTime = TimeUtil.nowMills();

    //关闭时间
    private long closeTime;
}
