package com.t13max.game.feature.active;

import com.t13max.game.feature.active.data.ActivityData;
import com.t13max.game.feature.active.data.IActFeature;
import com.t13max.data.manager.AsyncSaveManager;
import com.t13max.game.feature.active.enums.ActModelEnum;
import com.t13max.game.memory.SingleMemory;
import com.t13max.template.helper.ActivityHelper;
import com.t13max.template.manager.TemplateManager;
import com.t13max.template.temp.TemplateActivity;
import com.t13max.util.Log;
import game.entity.ActivityDataListPb;

/**
 * @author: t13max
 * @since: 20:42 2024/6/4
 */
public class ActivityMemory extends SingleMemory<ActivityDataListPb, ActivityData> {

    @Override
    public void init() {
        this.data = new ActivityData();
        AsyncSaveManager.inst().save(data);
        checkActivity();
    }

    @Override
    public void load() {
        super.load();
        checkActivity();
    }

    @Override
    public Class<ActivityData> getDataClazz() {
        return ActivityData.class;
    }

    @Override
    public ActivityDataListPb buildPb() {
        ActivityDataListPb.Builder builder = ActivityDataListPb.newBuilder();
        for (IActFeature iActFeature : this.data.getActivityDataMap().values()) {
            builder.addActivityDataList(iActFeature.toProto());
        }
        return builder.build();
    }

    private void checkActivity() {

        ActivityHelper activityHelper = TemplateManager.inst().helper(ActivityHelper.class);
        //关闭的活动
        for (Integer activityId : ActivityManager.inst().getClosedAct().keySet()) {
            TemplateActivity templateActivity = activityHelper.getTemplate(activityId);
            if (templateActivity == null) {
                Log.game.error("活动开启失败, template不存在, act={}, model={}", activityId);
                continue;
            }
            ActModelEnum actModelEnum = ActModelEnum.getActModelEnum(templateActivity.getType());
            if (actModelEnum == null) {
                Log.game.error("活动开启失败, model不存在, act={}, model={}", templateActivity.getId(), templateActivity.getType());
                continue;
            }
            actModelEnum.getActModel().onEnd(player, templateActivity);
        }

        for (Integer activityId : ActivityManager.inst().getActiveAct().keySet()) {
            TemplateActivity templateActivity = activityHelper.getTemplate(activityId);
            if (templateActivity == null) {
                Log.game.error("活动开启失败, template不存在, act={}, model={}", activityId);
                continue;
            }
            ActModelEnum actModelEnum = ActModelEnum.getActModelEnum(templateActivity.getType());
            if (actModelEnum == null) {
                Log.game.error("活动开启失败, model不存在, act={}, model={}", templateActivity.getId(), templateActivity.getType());
                continue;
            }
            actModelEnum.getActModel().onStart(player, templateActivity);
        }
    }

}
