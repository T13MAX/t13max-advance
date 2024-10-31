package com.t13max.game.feature.activity;

import com.t13max.data.mongo.MongoManager;
import com.t13max.game.feature.activity.data.ActivityData;
import com.t13max.game.feature.activity.data.IActFeature;
import com.t13max.game.feature.activity.enums.ActModelEnum;
import com.t13max.game.memory.SingleMemory;
import com.t13max.game.util.Log;
import com.t13max.template.helper.ActivityHelper;
import com.t13max.template.manager.TemplateManager;
import com.t13max.template.temp.TemplateActivity;
import game.entity.ActivityDataListPb;

/**
 * 活动内存数据
 *
 * @author: t13max
 * @since: 20:42 2024/6/4
 */
public class ActivityMemory extends SingleMemory<ActivityDataListPb, ActivityData> {

    @Override
    public void init() {
        this.data = new ActivityData(player.getRoleId());
        MongoManager.inst().save(data);
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

    /**
     * 加载数据后 检查活动的开启和关闭
     *
     * @Author t13max
     * @Date 16:34 2024/9/4
     */
    private void checkActivity() {

        ActivityHelper activityHelper = TemplateManager.inst().helper(ActivityHelper.class);
        //关闭的活动
        for (Integer activityId : ActivityManager.inst().getClosedAct().keySet()) {
            TemplateActivity templateActivity = activityHelper.getTemplate(activityId);
            if (templateActivity == null) {
                Log.game.error("活动关闭失败, template不存在, act={}", activityId);
                continue;
            }
            ActModelEnum actModelEnum = ActModelEnum.getActModelEnum(templateActivity.type);
            if (actModelEnum == null) {
                Log.game.error("活动关闭失败, model不存在, act={}, model={}", templateActivity.getId(), templateActivity.type);
                continue;
            }
            actModelEnum.getActModel().onEnd(player, this, templateActivity);
        }

        for (Integer activityId : ActivityManager.inst().getActiveAct().keySet()) {
            TemplateActivity templateActivity = activityHelper.getTemplate(activityId);
            if (templateActivity == null) {
                Log.game.error("活动开启失败, template不存在, act={}", activityId);
                continue;
            }
            ActModelEnum actModelEnum = ActModelEnum.getActModelEnum(templateActivity.type);
            if (actModelEnum == null) {
                Log.game.error("活动开启失败, model不存在, act={}, model={}", templateActivity.getId(), templateActivity.type);
                continue;
            }
            actModelEnum.getActModel().onStart(player, this, templateActivity);
        }
    }

}
