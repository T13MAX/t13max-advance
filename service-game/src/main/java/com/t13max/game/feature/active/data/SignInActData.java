package com.t13max.game.feature.active.data;

import game.entity.ActivityDataPb;
import game.entity.SignInActPb;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: t13max
 * @since: 20:45 2024/6/4
 */
@Data
public class SignInActData extends AbstractActData {

    private Set<Integer> signInSet;

    public SignInActData() {
        this.signInSet = new HashSet<>();
    }

    @Override
    public ActivityDataPb toProto() {

        SignInActPb.Builder builder = SignInActPb.newBuilder();
        builder.addAllSignInList(signInSet);

        return baseBuilder().setSignInActPb(builder).build();
    }
}
