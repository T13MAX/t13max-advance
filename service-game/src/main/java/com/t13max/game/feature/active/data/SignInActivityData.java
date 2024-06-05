package com.t13max.game.feature.active.data;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: t13max
 * @since: 20:45 2024/6/4
 */
@Data
public class SignInActivityData extends AbstractActivityData {

    private Set<Integer> signInSet;

    public SignInActivityData() {
        this.signInSet = signInSet;
    }
}
