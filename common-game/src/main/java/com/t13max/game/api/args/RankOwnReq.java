package com.t13max.game.api.args;

import com.t13max.game.enums.RankEnum;
import lombok.Data;

/**
 * @author t13max
 * @since 16:01 2024/11/4
 */
@Data
public class RankOwnReq {

    private RankEnum rankEnum;
    private int param1 = -1;
    private int serverId;
    private long playerId;
}
