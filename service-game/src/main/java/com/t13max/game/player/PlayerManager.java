package com.t13max.game.player;

import com.t13max.common.action.ActionExecutor;
import com.t13max.common.manager.ManagerBase;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: t13max
 * @since: 20:00 2024/5/23
 */
public class PlayerManager extends ManagerBase {

    private final Map<Long, Player> playerMap = new ConcurrentHashMap<>();

    @Getter
    private ActionExecutor actionExecutor;

    /**
     * 获取当前实例对象
     *
     * @Author t13max
     * @Date 16:44 2024/5/23
     */
    public static PlayerManager inst() {
        return ManagerBase.inst(PlayerManager.class);
    }

    @Override
    protected void onShutdown() {
        actionExecutor.shutdown();
    }

    @Override
    public void init() {
        actionExecutor = ActionExecutor.createExecutor();
    }

    public Player getPlayer(long uuid) {
        return playerMap.get(uuid);
    }

    public Player removePlayer(long uuid) {
        return playerMap.remove(uuid);
    }

    public List<Player> getOnlinePlayerList() {
        return new LinkedList<>(playerMap.values());
    }
}
