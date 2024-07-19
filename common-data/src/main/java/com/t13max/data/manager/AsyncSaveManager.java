package com.t13max.data.manager;

import com.t13max.common.manager.ManagerBase;
import com.t13max.data.dao.MongoUtil;
import com.t13max.data.entity.IData;
import com.t13max.game.util.Log;
import com.t13max.util.ThreadNameFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: t13max
 * @since: 19:29 2024/6/4
 */
public class AsyncSaveManager extends ManagerBase {

    public static final int INTERVAL = 100;

    private ScheduledExecutorService executorService;

    private LinkedBlockingQueue<IData> dataQueue = new LinkedBlockingQueue<>();

    private LinkedBlockingQueue<IData> deleteQueue = new LinkedBlockingQueue<>();

    private AtomicBoolean running = new AtomicBoolean(false);

    public static AsyncSaveManager inst() {
        return inst(AsyncSaveManager.class);
    }

    @Override
    protected void onShutdown() {
        shutdownSave();
        executorService.shutdown();
    }

    @Override
    public void init() {
        executorService = Executors.newSingleThreadScheduledExecutor(new ThreadNameFactory("data"));
        executorService.scheduleAtFixedRate(this::tickSave, 0, INTERVAL, TimeUnit.MILLISECONDS);
    }

    private void tickSave() {
        if (running.get()) {
            return;
        }
        running.set(true);
        try {
            List<IData> dataList = new LinkedList<>();
            dataQueue.drainTo(dataList);
            if (!dataList.isEmpty()) {
                dataList.parallelStream().forEach(MongoUtil::save);
            }

            List<IData> deleteList = new LinkedList<>();
            deleteQueue.drainTo(deleteList);
            if (!deleteList.isEmpty()) {
                deleteList.parallelStream().forEach(e -> MongoUtil.delete(e.getId(), e.getClass()));
            }
        } catch (Exception e) {
            Log.data.error("tickSave error! error={}", e.getMessage());
        } finally {
            running.set(false);
        }

    }

    private void shutdownSave() {
        while (!dataQueue.isEmpty() || !deleteQueue.isEmpty()) {
            tickSave();
        }
    }

    public <T extends IData> void delete(T data) {
        try {
            deleteQueue.add(data);
        } catch (Exception e) {
            Log.data.error("删除数据出错, data={}", data);
        }
    }

    public <T extends IData> void batchSave(List<T> dataList) {
        for (T data : dataList) {
            save(data);
        }
    }

    public <T extends IData> void save(T data) {
        try {
            long startInvoke = System.currentTimeMillis();
            Constructor<? extends IData> constructor = data.getClass().getDeclaredConstructor();
            IData newData = constructor.newInstance();
            for (Field field : data.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(data);
                field.set(newData, value);
            }
            dataQueue.add(newData);
            long endInvoke = System.currentTimeMillis();
            Log.data.debug("save data, cost={}", endInvoke - startInvoke);
        } catch (Exception e) {
            Log.data.error("异步存储出错! data={}, error={}", data, e.getMessage());
        }
    }
}
