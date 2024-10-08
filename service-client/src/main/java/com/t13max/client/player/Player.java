package com.t13max.client.player;

import battle.api.DoActionReq;
import battle.api.DoActionResp;
import com.google.protobuf.MessageLite;
import com.t13max.client.client.NettyClient;
import com.t13max.client.entity.HeroEntity;
import com.t13max.client.entity.MatchEntity;
import com.t13max.client.player.task.SendMsgTask;
import com.t13max.client.msg.ClientSession;
import com.t13max.client.view.panel.HeroPanel;
import com.t13max.client.view.window.*;
import com.t13max.common.msg.MessagePack;
import com.t13max.game.util.Log;
import com.t13max.template.helper.HeroHelper;
import com.t13max.template.manager.TemplateManager;
import com.t13max.template.temp.TemplateHero;
import lombok.Getter;
import lombok.Setter;
import message.id.MessageId;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author: t13max
 * @since: 19:09 2024/5/29
 */
@Getter
@Setter
public class Player {

    public static final Player PLAYER = new Player();

    private long uuid;

    private long matchId;

    private MatchEntity matchEntity;

    private BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

    private Map<Integer, BlockingQueue<MessagePack<? extends MessageLite>>> messageMap = new ConcurrentHashMap<>();

    private Map<String, AbstractWindow> windowMap = new HashMap<>();

    //玩家主线程
    private ExecutorService playerExecutor = Executors.newSingleThreadExecutor();

    private NettyClient nettyClient = new NettyClient();

    private ClientSession clientSession;

    private volatile boolean stop;

    private volatile long lastTickMills;

    private long curHeroId;

    private int skillIndex;

    private int targetIndex;

    public Player() {

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        nettyClient.start();
        try {
            nettyClient.connect();
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

    public void doAction() {
        if (curHeroId == 0) {
            return;
        }
        HeroEntity heroEntity = this.matchEntity.getSelfPlayer().getHeroMap().get(curHeroId);
        if (heroEntity == null) {
            Log.client.error("英雄不存在, heroId={}", curHeroId);
            return;
        }
        HeroEntity targetHero = this.matchEntity.getTargetHero(targetIndex);
        if (targetHero == null) {
            Log.client.error("英雄不存在, targetIndex", targetIndex);
            return;
        }
        TemplateHero template = TemplateManager.inst().helper(HeroHelper.class).getTemplate(heroEntity.getTemplateId());
        if (template == null) {
            Log.client.error("模板不存在, heroId={}, templateId={}", curHeroId, heroEntity.getTemplateId());
            return;
        }
        if (template.skill.size() <= skillIndex) {
            Log.client.error("技能选择错误, heroId={}, skillIndex={}, template={}", curHeroId, skillIndex, template.skill);
            return;
        }
        int skillId = template.skill.get(skillIndex);
        DoActionReq.Builder builder = DoActionReq.newBuilder();
        builder.setHeroId(this.curHeroId);
        builder.setSkillId(skillId);
        builder.addTargetIds(targetHero.getHeroId());
        sendMessage(MessageId.C_MATCH_ACTION_VALUE, builder.build());
        clearAction(heroEntity);
    }

    private void clearAction(HeroEntity heroEntity) {
        this.skillIndex = 0;
        this.targetIndex = 0;
        this.curHeroId = 0;
        HeroPanel heroPanel = heroEntity.getPanel();
        heroPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        heroPanel.repaint();
    }

    public void actionHero(long heroId) {
        this.curHeroId = heroId;
        HeroEntity heroEntity = this.matchEntity.getSelfPlayer().getHeroMap().get(heroId);
        if (heroEntity == null) {
            Log.client.error("英雄不存在, heroId={}", heroId);
            return;
        }
        HeroPanel heroPanel = heroEntity.getPanel();
        if (heroPanel == null) {
            Log.client.error("panel不存在, heroId={}", heroId);
            return;
        }
        heroPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.green));
        heroPanel.repaint();
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
    public void openWindow(String name, boolean open) {
        AbstractWindow window = this.windowMap.get(name);
        if (window != null) {
            if (open) window.openWindow();
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
        if (open) window.openWindow();
    }

    public void openWindow(String name) {
        openWindow(name, true);
    }

    public <T extends AbstractWindow> T getWindow(String name) {
        return (T) windowMap.get(name);
    }
}
