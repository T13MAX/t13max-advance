package com.t13max.client.player;

/**
 * @author: t13max
 * @since: 14:46 2024/5/30
 */
public interface IPlayerTask extends Runnable {

    void setPlayer(Player player);

    Player getPlayer();
}
