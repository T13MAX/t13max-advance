package com.t13max.fight;

import com.t13max.template.manager.TemplateManager;

/**
 * @author: t13max
 * @since: 14:01 2024/5/23
 */
public class BattleApplication {

    public static void main(String[] args) {
        quickStart();
    }

    public static void quickStart() {
        TemplateManager templateManager = new TemplateManager();
        if (!templateManager.load()) {
            return;
        }
        FightManager fightManager = new FightManager();
        fightManager.init();
        fightManager.quickStart();
    }
}
