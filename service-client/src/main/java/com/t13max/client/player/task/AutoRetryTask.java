package com.t13max.client.player.task;

import com.t13max.client.player.Player;
import com.t13max.game.util.Log;
import lombok.Data;

/**
 * @author: t13max
 * @since: 14:45 2024/5/30
 */
@Data
public abstract class AutoRetryTask extends AbstractTask {

    private int retry;

    protected void retry() {
        retry++;
        if (retry > retryCount()) {
            Log.client.error("重试次数过多! task={}", this.getClass().getSimpleName());
            retryFail();
            return;
        }
        Player.PLAYER.addTask(this);
    }

    public void submit() {
        Player.PLAYER.addTask(this);
    }

    protected void retryFail() {

    }

    protected int retryCount(){
        return 100;
    }

}
