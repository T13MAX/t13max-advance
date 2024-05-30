package com.t13max.client.player.task;

import com.t13max.client.player.IPlayerTask;
import com.t13max.client.player.Player;
import com.t13max.util.Log;
import lombok.Data;

/**
 * @author: t13max
 * @since: 14:45 2024/5/30
 */
@Data
public abstract class AbstractTask implements IPlayerTask {

    protected Player player;

    private int retry;

    protected void retry() {
        retry++;
        if (retry > 100) {
            Log.client.error("重试次数过多!");
        }
        Player.PLAYER.addTask(this);
    }

}
