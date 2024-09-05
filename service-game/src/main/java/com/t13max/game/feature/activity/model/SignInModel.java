package com.t13max.game.feature.activity.model;

import com.t13max.game.feature.activity.data.ActivityData;
import com.t13max.game.feature.activity.data.IActFeature;
import com.t13max.game.feature.activity.ActivityMemory;
import com.t13max.game.feature.activity.enums.ActModelEnum;
import com.t13max.game.player.Player;
import com.t13max.template.temp.TemplateActivity;

/**
 * 签到活动
 *
 * @author: t13max
 * @since: 16:34 2024/6/6
 */
public class SignInModel extends AbstractModel {

    public SignInModel() {
       super(ActModelEnum.SIGN_IN);
    }

    @Override
    public void onStart(Player player,ActivityMemory activityMemory, TemplateActivity activity) {
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
    public void onEnd(Player player,ActivityMemory activityMemory, TemplateActivity activity) {
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
