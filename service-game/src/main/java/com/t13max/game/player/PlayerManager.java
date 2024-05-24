package com.t13max.game.player;

import com.t13max.game.config.ActionConfig;
import com.t13max.game.config.BaseConfig;
import com.t13max.game.manager.ManagerBase;
import com.t13max.game.run.Application;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: t13max
 * @since: 20:00 2024/5/23
 */
public class PlayerManager extends ManagerBase {

    private Map<Long, Player> playerMap = new ConcurrentHashMap<>();

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
        ActionConfig actionConfig = Application.config().getAction();
        actionExecutor = ActionExecutor.createExecutor(actionConfig.getCore(), actionConfig.getMax(), actionConfig.getName());
    }

    public Player getPlayer(long uuid) {
        return playerMap.get(uuid);
    }
}
