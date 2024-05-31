package com.t13max.client.player;

import com.google.protobuf.MessageLite;
import com.t13max.client.client.NettyClient;
import com.t13max.client.entity.MatchEntity;
import com.t13max.client.player.task.SendMsgTask;
import com.t13max.client.msg.ClientSession;
import com.t13max.client.view.window.*;
import com.t13max.game.msg.MessagePack;
import com.t13max.util.Log;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author: t13max
 * @since: 19:09 2024/5/29
 */
@Getter
public class Player {

    public static final Player PLAYER = new Player();

    @Setter
    private long uuid;
    @Setter
    private long matchId;
    @Setter
    private MatchEntity matchEntity;

    private BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

    private Map<Integer, BlockingQueue<MessagePack<? extends MessageLite>>> messageMap = new ConcurrentHashMap<>();

    private Map<String, AbstractWindow> windowMap = new HashMap<>();

    //玩家主线程
    private ExecutorService playerExecutor = Executors.newSingleThreadExecutor();

    private NettyClient nettyClient = new NettyClient();

    @Setter
    private ClientSession clientSession;

    private volatile boolean stop;

    private volatile long lastTickMills;

    public Player() {

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        nettyClient.start();
        try {
            nettyClient.connect("127.0.0.1", 24000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        playerExecutor.execute(this::run);
    }

    public void run() {

        while (!stop) {

            long beginMills = System.currentTimeMillis();

            try {
                tick();
            } catch (Exception e) {
                Log.client.error("tick出错 error={}", e.getMessage());
            }

            long endMills = System.currentTimeMillis();

            this.lastTickMills = endMills;

            //运行时间
            long runMills = endMills - beginMills;

            try {
                long sleepMills = Math.max(0, 200 - runMills);
                Thread.sleep(sleepMills);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void tick() {
        if (taskQueue.isEmpty()) {
            return;
        }
        List<Runnable> list = new LinkedList<>();
        taskQueue.drainTo(list);

        for (Runnable runnable : list) {
            runnable.run();
        }
    }

    public void shutdown() {
        this.stop = true;
        playerExecutor.shutdown();
        try {
            if (!playerExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                playerExecutor.shutdownNow();
                if (!playerExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                    Log.client.error("player线程池关不掉!");
                }
            }
        } catch (InterruptedException e) {
            Log.client.error("terminate被中断!");
        }

        clientSession.close();
    }

    public void sendMessage(int msgId, MessageLite messageLite) {
        this.addTask(new SendMsgTask(msgId, messageLite));
    }

    public void addTask(IPlayerTask playerTask) {
        if (stop) {
            Log.client.error("已经stop 拒绝任务!, runnable={}", playerTask);
            return;
        }

        playerTask.setPlayer(this);

        if (!this.taskQueue.offer(playerTask)) {
            Log.client.error("addTask failed!, runnable={}", playerTask);
        }
    }

    /**
     * 打开窗口
     * 目前窗口也不多 就这样吧
     *
     * @Author t13max
     * @Date 14:16 2024/5/31
     */
    public void openWindow(String name) {
        AbstractWindow window = this.windowMap.get(name);
        if (window != null) {
            window.openWindow();
            return;
        }
        switch (name) {
            case "login" -> {
                window = new LoginWindow();
            }
            case "home" -> {
                window = new HomeWindow();
            }
            case "log" -> {
                window = new LogWindow();
            }
            case "detail" -> {
                window = new HeroDetailWindow();
            }
            default -> {
                return;
            }
        }
        this.windowMap.put(name, window);
        window.openWindow();
    }

    public <T extends AbstractWindow> T getWindow(String name) {
        return (T) windowMap.get(name);
    }
}
