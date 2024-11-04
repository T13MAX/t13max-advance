package com.t13max.game.api.args;

import com.t13max.game.api.entity.RankInfo;
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
public class UpdateRankReq {

    Map<String, Map<Integer, List<RankInfo>>> rankDataMap = new HashMap<>();
}
