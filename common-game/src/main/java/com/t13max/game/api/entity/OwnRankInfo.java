package com.t13max.game.api.entity;

import lombok.Data;

/**
 * @author t13max
 * @since 16:00 2024/11/4
 */
@Data
public class OwnRankInfo {

    private PlayerBasicInfo playerBasicInfo;  //角色基础信息
    private int rank;
    private long score;
}
