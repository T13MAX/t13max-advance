package com.t13max.fight;

import com.t13max.fight.event.FightEventBus;
import com.t13max.fight.log.FightLogManager;
import lombok.Getter;
import lombok.Setter;

/**
 * 战斗上下文 获取各种组件
 *
 * @author: t13max
 * @since: 15:13 2024/5/27
 */
@Getter
@Setter
public class FightContext {

    private FightEventBus fightEventBus;

    private FightMatch fightMatch;

    private FightTimeMachine fightTimeMachine;

    private FightLogManager fightLogManager;

}
