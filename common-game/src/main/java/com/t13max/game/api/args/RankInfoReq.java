package com.t13max.game.api.args;

import com.t13max.game.enums.RankEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 排行榜更新参数
 *
 * @author t13max
 * @since 15:28 2024/10/31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankInfoReq {

    private RankEnum rankEnum;
    private int param1 = -1;
    private int serverId;
    private long playerId;
}
