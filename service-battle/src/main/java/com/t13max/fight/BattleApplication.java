package com.t13max.fight;

import com.t13max.game.manager.ManagerBase;
import com.t13max.template.helper.HeroHelper;
import com.t13max.template.manager.TemplateManager;
import com.t13max.template.temp.TemplateHero;
import com.t13max.util.Log;

/**
 * @author: t13max
 * @since: 14:01 2024/5/23
 */
public class BattleApplication {

    public static void main(String[] args) {
        quickStart();
    }

    public static void quickStart() {

        //初始化所有Manager
        ManagerBase.initAllManagers();

        HeroHelper heroHelper = TemplateManager.inst().helper(HeroHelper.class);
        TemplateHero template = heroHelper.getTemplate(100001);
        Log.battle.info("quickStart");
        /*FightManager fightManager = new FightManager();
        fightManager.init();
        fightManager.quickStart();*/
    }
}
