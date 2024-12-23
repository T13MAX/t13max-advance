package com.t13max.game.player;

import com.google.protobuf.MessageLite;
import com.t13max.common.action.ActionQueue;
import com.t13max.common.action.IJobName;
import com.t13max.data.entity.RoleData;
import com.t13max.data.mongo.IData;
import com.t13max.game.memory.MemoryTable;
import com.t13max.game.memory.IMemory;
import com.t13max.game.util.Log;
import game.entity.Sync;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 19:59 2024/5/23
 */
@Getter
@Setter
public class Player {

    //玩家的唯一id
    private final long uuid;

    //当前所选角色id
    private final long roleId;

    //角色数据实体
    private final RoleData roleData;

    //玩家自己的任务队列
    private final ActionQueue actionQueue;

    private final Sync.Builder sync = Sync.newBuilder();

    private final Map<Class<? extends IMemory<? extends MessageLite, ? extends IData>>, IMemory<? extends MessageLite, ? extends IData>> memoryMap;

    public Player(RoleData roleData) {
        this.uuid = roleData.getUuid();
        this.roleId = roleData.getRoleId();
        this.roleData = roleData;
        this.actionQueue = new ActionQueue(PlayerManager.inst().getActionExecutor());
        this.memoryMap = new HashMap<>();
    }

    /**
     * 在玩家的任务队列执行
     *
     * @Author t13max
     * @Date 20:39 2024/6/4
     */
    public void execute(IJobName jobName, Runnable action) {
        actionQueue.execute(jobName, action);
    }

    /**
     * 获取玩家某个功能的内存数据
     * 自动从库加载/初始化
     *
     * @Author t13max
     * @Date 20:37 2024/6/4
     */
    public <PB extends MessageLite, DATA extends IData, M extends IMemory<PB, DATA>> M getMemory(Class<M> clazz) {
        M instance = (M) memoryMap.get(clazz);
        if (instance != null) {
            return instance;
        }
        MemoryTable memoryTable = MemoryTable.getData(clazz);
        if (memoryTable == null) {
            Log.game.error("获取玩家Memory失败, memory={}", clazz.getSimpleName());
            return null;
        }
        byte memoryId = memoryTable.getId();
        if (!MemoryTable.isValid(memoryId)) {
            Log.game.error("获取玩家Memory失败, memory={}", clazz.getSimpleName());
            return null;
        }
        instance = (M) memoryTable.getSupplier().get();
        instance.setOwner(this);
        if (this.isMemoryUnlocked(memoryId)) {
            instance.init();
            this.unlockMemory(memoryId);
        } else {
            instance.load();
        }
        memoryMap.put(clazz, instance);
        return instance;
    }

    private boolean isMemoryUnlocked(byte managerId) {
        if (managerId < 0) {
            return false;
        }
        return (roleData.getUnlockMemory() & (0x1L << managerId)) > 0;
    }

    private void unlockMemory(byte managerId) {
        if (managerId < 0) {
            return;
        }
        synchronized (this) {
            this.roleData.setUnlockMemory(this.roleData.getUnlockMemory() | (0x1L << managerId));
        }
    }

    /**
     * 发送消息
     *
     * @Author t13max
     * @Date 17:02 2024/9/4
     */
    public void sendMsg(int msgId, MessageLite messageLite) {

    }

    /**
     * 直接发送sync
     *
     * @Author t13max
     * @Date 17:01 2024/9/4
     */
    public void sendSync() {

    }

    /**
     * 生成sync的pb
     *
     * @Author t13max
     * @Date 17:02 2024/9/4
     */
    public Sync genSync() {
        Sync build = sync.build();
        //清理sync
        return build;
    }
}
