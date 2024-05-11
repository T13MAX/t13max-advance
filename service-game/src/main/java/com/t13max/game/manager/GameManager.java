package com.t13max.game.manager;

import com.t13max.fight.FightManager;
import com.t13max.game.exception.GameException;
import lombok.Data;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.LockSupport;

/**
 * @author: t13max
 * @since: 14:34 2024/4/11
 */
@Data
public class GameManager {

    private TemplateManager templateManager;

    private FightManager fightManager;

    public GameManager() {

        this.templateManager = new TemplateManager();
        if (!this.templateManager.load()) {
            throw new GameException("表加载出错");
        }

        this.fightManager = new FightManager();
        this.fightManager.init();

    }

    public void quickStart() {

        //this.templateManager.test();

        this.fightManager.quickStart();

    }
}
