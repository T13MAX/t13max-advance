package com.t13max.client.player;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import com.t13max.client.client.NettyClient;
import com.t13max.client.player.task.SendMsgTask;
import com.t13max.client.player.task.msg.MessagePack;
import com.t13max.util.Log;
import io.netty.channel.Channel;
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

    public static Map<Integer, Parser<? extends MessageLite>> PARSER_MAP = new HashMap<>();

    @Setter
    private long uuid;

    private BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

    private Map<Integer, BlockingQueue<MessagePack<? extends MessageLite>>> messageMap = new ConcurrentHashMap<>();

    //玩家主线程
    private ExecutorService playerExecutor = Executors.newSingleThreadExecutor();

    private NettyClient nettyClient = new NettyClient();

    private Channel channel;

    private volatile boolean stop;

    private volatile long lastTickMills;

    public Player() {

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        nettyClient.start();
        try {
            channel = nettyClient.connect("127.0.0.1", 24000).channel();
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

        channel.close();
    }

    public <T extends MessageLite> void receiveMessage(int msgId, int resCode, byte[] data) {
        MessagePack<T> messagePack;
        if (resCode != 0) {
            messagePack = new MessagePack<>(resCode);
        } else {
            T object = null;
            try {
                Parser<T> parser = (Parser<T>) PARSER_MAP.get(msgId);
                object = parser.parseFrom(data);
            } catch (InvalidProtocolBufferException e) {
                Log.client.error("解析错误, msgId={}", msgId);
                return;
            }
            messagePack = new MessagePack<>(object);
        }
        BlockingQueue<MessagePack<?>> messagePacks = this.messageMap.computeIfAbsent(msgId, k -> new LinkedBlockingQueue<>());
        if (!messagePacks.offer(messagePack)) {
            Log.client.error("MessagePack 天加失败");
        }
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

    public <T extends MessageLite> List<MessagePack<T>> getMessage(int msgId) {
        BlockingQueue<MessagePack<? extends MessageLite>> messagePacks = messageMap.get(msgId);
        if (messagePacks == null || messagePacks.isEmpty()) {
            return Collections.emptyList();
        }
        List<MessagePack<T>> result = new LinkedList<>();
        messagePacks.drainTo((List) result);
        return result;
    }

}
