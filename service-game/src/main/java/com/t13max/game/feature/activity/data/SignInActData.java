package com.t13max.game.feature.activity.data;

import game.entity.ActivityDataPb;
import game.entity.SignInActPb;
import lombok.Data;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * 签到活动数据
 *
 * @author: t13max
 * @since: 20:45 2024/6/4
 */
@Getter
public class SignInActData extends AbstractActData {

    private final Set<Integer> signInSet= new HashSet<>();

    public SignInActData(int activityId, int type) {
        super(activityId,type);
    }

    @Override
    public ActivityDataPb toProto() {

        SignInActPb.Builder builder = SignInActPb.newBuilder();
        builder.addAllSignInList(signInSet);

        return baseBuilder().setSignInActPb(builder).build();
    }
}
