package com.t13max.game.player;

import com.t13max.data.entity.RoleData;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: t13max
 * @since: 19:59 2024/5/23
 */
@Getter
@Setter
public class Player {

    //玩家的唯一id
    private long uuid;

    //当前所选角色id
    private long roleId;

    //角色数据实体
    private RoleData roleData;

    //玩家自己的任务队列
    private ActionQueue actionQueue;


    public Player(long uuid) {
        this.uuid = uuid;
        actionQueue = new ActionQueue(PlayerManager.inst().getActionExecutor());
    }

    public void execute(JobName jobName, Runnable action) {
        actionQueue.execute(jobName, action);
    }
}
