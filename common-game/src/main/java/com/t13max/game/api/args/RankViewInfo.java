package com.t13max.game.api.args;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 排行榜信息
 *
 * @author t13max
 * @since 15:34 2024/10/31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankViewInfo {

    private int rank;
    private RankInfo rankData = new RankInfo();
    private PlayerBasicInfo playerBasicInfo = new PlayerBasicInfo();
}
