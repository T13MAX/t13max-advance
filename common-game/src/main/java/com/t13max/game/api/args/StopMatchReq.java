package com.t13max.game.api.args;

import lombok.Data;

/**
 * @author t13max
 * @since 16:18 2024/11/4
 */
@Data
public class StopMatchReq {

    private final long uuid;

    public StopMatchReq(long uuid) {
        this.uuid = uuid;
    }
}
