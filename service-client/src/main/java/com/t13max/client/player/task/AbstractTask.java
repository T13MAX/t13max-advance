package com.t13max.client.player.task;

import com.t13max.client.player.IPlayerTask;
import com.t13max.client.player.Player;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: t13max
 * @since: 11:47 2024/6/6
 */
@Getter
@Setter
public abstract class AbstractTask implements IPlayerTask {

    protected Player player;

    public void submit() {
        Player.PLAYER.addTask(this);
    }
}
