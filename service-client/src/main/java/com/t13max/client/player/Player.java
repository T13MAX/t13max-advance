package com.t13max.client.player;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import com.t13max.client.player.task.SendMsgTask;
import com.t13max.client.player.task.msg.MessagePack;
import com.t13max.client.view.login.LoginFrame;
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

    private BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

    private Map<Integer, BlockingQueue<MessagePack<? extends MessageLite>>> messageMap = new ConcurrentHashMap<>();

    private Map<Integer, Parser<? extends MessageLite>> parserMap = new HashMap<>();

    //玩家主线程
    private ExecutorService playerExecutor = Executors.newSingleThreadExecutor();

    private volatile boolean stop;

    public Player() {

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        playerExecutor.execute(this::run);
    }

    public void run() {

    }

    public void tick() {

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
    }

    public <T extends MessageLite> void receiveMessage(int msgId, int resCode, byte[] data) {
        MessagePack<T> messagePack;
        if (resCode != 0) {
            messagePack = new MessagePack<>(resCode);
        } else {
            T object = null;
            try {
                Parser<T> parser = (Parser<T>) parserMap.get(msgId);
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
