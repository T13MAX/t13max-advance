package com.t13max.game.feature.activity.model;

import com.t13max.game.feature.activity.data.IActFeature;
import com.t13max.game.feature.activity.enums.ActModelEnum;
import com.t13max.template.temp.TemplateActivity;
import lombok.Getter;

/**
 * @author: t13max
 * @since: 16:38 2024/6/6
 */
@Getter
public abstract class AbstractModel implements IActModel {

    protected final ActModelEnum modelEnum;

    public AbstractModel(ActModelEnum modelEnum) {
        this.modelEnum = modelEnum;
    }

    protected <T extends IActFeature> T createData(TemplateActivity activity) {
        IActFeature featureData = modelEnum.createData(activity);
        return (T) featureData;
    }

}
