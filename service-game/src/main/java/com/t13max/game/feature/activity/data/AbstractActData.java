package com.t13max.game.feature.activity.data;

import com.t13max.game.exception.GameException;
import com.t13max.game.feature.activity.ActivityManager;
import com.t13max.template.helper.ActivityHelper;
import com.t13max.template.manager.TemplateManager;
import com.t13max.template.temp.TemplateActivity;
import com.t13max.util.TimeUtil;
import game.entity.ActivityDataPb;
import lombok.Data;

/**
 * @author: t13max
 * @since: 20:53 2024/6/4
 */
@Data
public abstract class AbstractActData implements IActFeature {
    //活动id
    private int activityId;

    //活动类型
    private int type;

    //创建时间
    private long createMills = TimeUtil.nowMills();

    protected ActivityDataPb.Builder baseBuilder() {
        TemplateActivity templateActivity = TemplateManager.inst().helper(ActivityHelper.class).getTemplate(activityId);
        if (templateActivity == null) {
            throw new GameException("活动配置找不到, activityId=" + activityId);
        }
        long startMills = ActivityManager.inst().calcStartMills(templateActivity);
        long endMills = ActivityManager.inst().calcEndMills(templateActivity);
        ActivityDataPb.Builder builder = ActivityDataPb.newBuilder();
        builder.setActId(activityId);
        builder.setStartMills(startMills);
        builder.setEndMills(endMills);
        builder.setType(type);
        return builder;
    }

}
