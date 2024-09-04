package com.t13max.game.feature.activity.model;

import com.t13max.game.feature.activity.enums.ActModelEnum;
import com.t13max.game.player.Player;
import com.t13max.template.temp.TemplateActivity;

/**
 * @author: t13max
 * @since: 16:35 2024/6/6
 */
public interface IActModel {

    ActModelEnum getModelEnum();

    void onStart(Player player, TemplateActivity activity);

    void onEnd(Player player, TemplateActivity activity);

    void onDay0Refresh(Player player, TemplateActivity activity);
}
