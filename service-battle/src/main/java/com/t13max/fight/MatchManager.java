package com.t13max.fight;

import battle.api.CreateFightMatchReq;
import battle.entity.FightHeroInfoPb;
import battle.entity.FightPlayerInfoPb;
import com.t13max.fight.enums.FightEnum;
import com.t13max.game.manager.ManagerBase;
import com.t13max.util.Log;
import com.t13max.util.ThreadNameFactory;
import com.t13max.util.TimeUtil;
import com.t13max.util.UuidUtil;
import lombok.extern.log4j.Log4j2;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author: t13max
 * @since: 13:41 2024/4/11
 */
@Log4j2
public class MatchManager extends ManagerBase {

    private ExecutorService fightExecutor;

    private Map<Long, FightMatch> fightMatchMap = new ConcurrentHashMap<>();

    private Set<FightMatch> finishedList = new HashSet<>();

    private volatile boolean stop;

    private static final int INTERVAL = 200;

    private boolean init;

    public MatchManager() {

    }

    /**
     * 获取当前实例对象
     *
     * @Author t13max
     * @Date 16:44 2024/5/23
     */
    public static MatchManager inst() {
        return ManagerBase.inst(MatchManager.class);
    }

    @Override
    protected void onShutdown() {

        this.stop = true;
        this.fightExecutor.shutdown();
        try {
            if (!fightExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                fightExecutor.shutdownNow();
                if (!fightExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                    Log.battle.error("战斗tick线程shutdown失败!");
                }
            }
        } catch (InterruptedException e) {
            Log.battle.error("战斗tick线程shutdown中断!");
        }
        super.onShutdown();
    }

    @Override
    public void init() {

        if (init) return;

        this.fightExecutor = Executors.newSingleThreadExecutor(new ThreadNameFactory("fight-tick"));

        this.fightExecutor.execute(this::tick);

        this.init = true;
    }

    private void tick() {

        while (!stop) {
            if (fightMatchMap.isEmpty()) {
                try {
                    Thread.sleep(1 * 1000);
                } catch (InterruptedException e) {

                }
                continue;
            }

            long beginMills = TimeUtil.nowMills();

            for (FightMatch fightMatch : fightMatchMap.values()) {
                try {
                    fightMatch.tick();
                    if (fightMatch.getFightEnum() == FightEnum.FINISHED) {
                        fightMatch.finish();
                        this.finishedList.add(fightMatch);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    fightMatch.getFightContext().getFightLogManager().forcePrint();
                    this.finishedList.add(fightMatch);
                }
            }

            if (!finishedList.isEmpty()) {
                destroyFight();
            }

            long endMills = TimeUtil.nowMills();

            long interval = endMills - beginMills;

            if (interval > INTERVAL) {
                log.error("FightManager, tick用时过长, interval={}", interval);
                continue;
            }

            try {
                Thread.sleep(INTERVAL - interval);
            } catch (InterruptedException e) {

            }

        }

    }

    private void destroyFight() {
        for (FightMatch fight : this.finishedList) {
            this.fightMatchMap.remove(fight.getId());
        }
    }

    public void addFightMatch(FightMatch fightMatch) {
        this.fightMatchMap.put(fightMatch.getId(), fightMatch);
    }

    public void removeFightMatch(FightMatch fightMatch) {
        this.fightMatchMap.remove(fightMatch.getId());
    }

    public FightMatch getFightMatch(long matchId) {
        return this.fightMatchMap.get(matchId);
    }

}
