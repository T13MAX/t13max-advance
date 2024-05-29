package com.t13max.fight.event;

import battle.entity.FightEventPb;

/**
 * 需要打包发给前端的事件接口
 *
 * @author: t13max
 * @since: 18:52 2024/5/28
 */
public interface IFightEventPackager {

    FightEventPb pack();
}
