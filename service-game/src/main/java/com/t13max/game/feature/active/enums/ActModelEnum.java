package com.t13max.game.feature.active.enums;

import com.t13max.game.feature.active.data.IActFeature;
import com.t13max.game.feature.active.data.SignInActData;
import com.t13max.game.feature.active.model.IActModel;
import com.t13max.game.feature.active.model.SignInModel;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author: t13max
 * @since: 16:33 2024/6/6
 */
@Getter
public enum ActModelEnum {
    SIGN_IN(1, SignInActData::new, new SignInModel()),
    ;

    private final int id;

    private final Supplier<? extends IActFeature> dataCreator;

    private final IActModel actModel;

    private static final Map<Integer, ActModelEnum> DATA_MAP = new HashMap<>();

    static {
        for (ActModelEnum actModelEnum : values()) {
            DATA_MAP.put(actModelEnum.getId(), actModelEnum);
        }
    }

    ActModelEnum(int id, Supplier<? extends IActFeature> dataCreator, IActModel actModel) {
        this.id = id;
        this.dataCreator = dataCreator;
        this.actModel = actModel;
    }

    public <T extends IActFeature> T createData() {
        return (T) dataCreator.get();
    }

    public static ActModelEnum getActModelEnum(int id) {
        return DATA_MAP.get(id);
    }
}
