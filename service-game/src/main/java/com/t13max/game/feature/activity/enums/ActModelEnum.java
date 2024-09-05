package com.t13max.game.feature.activity.enums;

import com.t13max.game.feature.activity.data.IActFeature;
import com.t13max.game.feature.activity.data.SignInActData;
import com.t13max.game.feature.activity.model.IActModel;
import com.t13max.game.feature.activity.model.SignInModel;
import com.t13max.template.temp.TemplateActivity;
import lombok.Getter;
import org.apache.poi.ss.formula.functions.T;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author: t13max
 * @since: 16:33 2024/6/6
 */
@Getter
public enum ActModelEnum {
    SIGN_IN(1,(activity)->new SignInActData(activity.getId(),1) , new SignInModel()),
    ;

    private final int id;

    private final ActDataCreator dataCreator;

    private final IActModel actModel;

    private static final Map<Integer, ActModelEnum> DATA_MAP = new HashMap<>();

    static {
        for (ActModelEnum actModelEnum : values()) {
            DATA_MAP.put(actModelEnum.getId(), actModelEnum);
        }
    }

    ActModelEnum(int id, ActDataCreator dataCreator, IActModel actModel) {
        this.id = id;
        this.dataCreator = dataCreator;
        this.actModel = actModel;
    }

    public IActFeature createData(TemplateActivity activity) {
        return dataCreator.createData(activity);
    }

    public static ActModelEnum getActModelEnum(int id) {
        return DATA_MAP.get(id);
    }

    public interface ActDataCreator {

        IActFeature createData(TemplateActivity activity);
    }

}
