package com.t13max.scene.manager;

import com.t13max.game.manager.ManagerBase;
import com.t13max.scene.object.role.SceneRole;
import com.t13max.scene.stage.Scene;
import com.t13max.scene.subline.SubLineHelper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 场景管理类
 *
 * @author: t13max
 * @since: 14:37 2024/5/23
 */
public class SceneManager extends ManagerBase {

    //所有场景的角色
    private final Map<Long, SceneRole> sceneRoleMap = new ConcurrentHashMap<>(8192);
    //所有的场景对象，包含静态和动态的
    private Map<Long, Scene> sceneMap = new ConcurrentHashMap<>(512);
    //分线管理
    private SubLineHelper subLineHelper = new SubLineHelper();

    public static SceneManager inst() {
        return inst(SceneManager.class);
    }

    @Override
    protected void onShutdown() {
        super.onShutdown();
    }

    @Override
    public List<Class<? extends ManagerBase>> getDependents() {
        return super.getDependents();
    }

    @Override
    public void init() {
        //初始化场景线程组
        /*MapThreadManager.inst().initThreads();
        updateLineNum(ServerConfig.getInstance().getLineNum());*/
    }
}
