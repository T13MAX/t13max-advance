package com.t13max.game.memory;

import com.t13max.game.player.Player;


/**
 * @author: t13max
 * @since: 18:58 2024/6/4
 */
public interface IMemory<PB, DATA> {

    // 免库初始化
    void init();

    // 读库初始化
    void load();

    void setOwner(Player player);

    void batchSave();

    Class<DATA> getDataClazz();

}
