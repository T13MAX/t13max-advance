package com.t13max.rank.manager;

import com.t13max.common.manager.ManagerBase;

import java.util.concurrent.*;

/**
 * @author t13max
 * @since 17:29 2024/9/4
 */
public class RankManager extends ManagerBase {

    private final ScheduledExecutorService autoSaveExecutor = Executors.newSingleThreadScheduledExecutor();

    private BlockingQueue updateQueue=new LinkedBlockingQueue<>();
    public RankManager inst() {
        return inst(RankManager.class);
    }

    @Override
    protected void onShutdown() {
        autoSaveExecutor.shutdown();
    }

    @Override
    protected void init() {
        //反序列化
        autoSaveExecutor.scheduleAtFixedRate(this::rankDump, 5, 5, TimeUnit.SECONDS);
    }

    private void rankDump() {

    }

    private void rankRestore() {

    }
}
