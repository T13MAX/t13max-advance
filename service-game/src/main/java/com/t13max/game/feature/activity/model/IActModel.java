package com.t13max.game.feature.activity.model;

import com.t13max.game.feature.activity.ActivityMemory;
import com.t13max.game.feature.activity.enums.ActModelEnum;
import com.t13max.game.player.Player;
import com.t13max.template.temp.TemplateActivity;

/**
 * @author: t13max
 * @since: 16:35 2024/6/6
 */
public interface IActModel {

    //获取活动类型枚举
    ActModelEnum getModelEnum();

    //活动开始
    void onStart(Player player, ActivityMemory activityMemory, TemplateActivity activity);

    //活动结束
    void onEnd(Player player, ActivityMemory activityMemory, TemplateActivity activity);

    void onDay0Refresh(Player player, TemplateActivity activity);
}
