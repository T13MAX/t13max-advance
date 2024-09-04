package com.t13max.game.feature.activity.model;

import com.t13max.game.feature.activity.data.ActivityData;
import com.t13max.game.feature.activity.data.IActFeature;
import com.t13max.game.feature.activity.ActivityMemory;
import com.t13max.game.player.Player;
import com.t13max.template.temp.TemplateActivity;

/**
 * @author: t13max
 * @since: 16:34 2024/6/6
 */
public class SignInModel extends AbstractModel {

    @Override
    public void onStart(Player player, TemplateActivity activity) {
        ActivityMemory activityMemory = player.getMemory(ActivityMemory.class);
        ActivityData activityData = activityMemory.get();
        IActFeature iActFeature = activityData.getActivityDataMap().get(activity.getId());
        if (iActFeature != null) {
            return;
        }
        iActFeature = createData(activity);
        activityData.getActivityDataMap().put(activity.getId(), iActFeature);
        //一些初始化操作
    }

    @Override
    public void onEnd(Player player, TemplateActivity activity) {
        ActivityMemory activityMemory = player.getMemory(ActivityMemory.class);
        ActivityData activityData = activityMemory.get();
        IActFeature iActFeature = activityData.getActivityDataMap().get(activity.getId());
        if (iActFeature == null) {
            return;
        }
        activityData.getActivityDataMap().remove(activity.getId());
    }

    @Override
    public void onDay0Refresh(Player player, TemplateActivity activity) {

    }
}
