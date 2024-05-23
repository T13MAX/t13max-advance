package com.t13max.fight;

import com.t13max.fight.enums.FightEnum;
import com.t13max.util.ThreadNameFactory;
import com.t13max.util.TimeUtil;
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
public class FightManager {

    private ExecutorService fightExecutor;

    private Map<Long, FightMatch> fightImplMap = new ConcurrentHashMap<>();

    private Set<FightMatch> finishedList = new HashSet<>();

    private volatile boolean stop;

    private static final int INTERVAL = 200;

    private boolean init;

    public FightManager() {

    }

    public void init() {

        if (init) return;

        this.fightExecutor = Executors.newSingleThreadExecutor(new ThreadNameFactory("fight-tick"));

        this.fightExecutor.execute(this::tick);

        this.init = true;
    }

    private void tick() {

        while (!stop) {
            if (fightImplMap.isEmpty()) {
                try {
                    Thread.sleep(1 * 1000);
                } catch (InterruptedException e) {

                }
                continue;
            }

            long beginMills = TimeUtil.nowMills();

            for (FightMatch fightMatch : fightImplMap.values()) {
                try {
                    fightMatch.tick();
                    if (fightMatch.getFightEnum() == FightEnum.FINISHED) {
                        fightMatch.finish();
                        this.finishedList.add(fightMatch);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    fightMatch.getFightLogManager().forcePrint();
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
            this.fightImplMap.remove(fight.getId());
        }
    }

    public void addFightImpl(FightMatch fightMatch) {
        this.fightImplMap.put(fightMatch.getId(), fightMatch);
    }

    public void removeFightImpl(FightMatch fightMatch) {
        this.fightImplMap.remove(fightMatch.getId());
    }

    public void quickStart() {
        FightMatch fightMatch = FightFactory.quickCreateFightImpl();
        this.addFightImpl(fightMatch);
    }
}
