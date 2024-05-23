package com.t13max.fight;

import com.t13max.game.manager.ManagerBase;

/**
 * @author: t13max
 * @since: 14:01 2024/5/23
 */
public class BattleApplication {

    public static void main(String[] args) {

        //初始化所有Manager
        ManagerBase.initAllManagers();

        FightManager.inst().quickStart();

    }

}
