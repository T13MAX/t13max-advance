package com.t13max.game.api.entity;

import lombok.Data;

/**
 * 玩家简要信息
 *
 * @author t13max
 * @since 15:47 2024/11/4
 */
@Data
public class PlayerBasicInfo {

    private long aid;
    private long pid;
    private String playerName;
}
