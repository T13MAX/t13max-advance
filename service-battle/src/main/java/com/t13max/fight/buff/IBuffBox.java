package com.t13max.fight.buff;

import com.t13max.fight.FightContext;
import com.t13max.fight.buff.effect.IBuffEffect;

import java.util.Set;

/**
 * @author: t13max
 * @since: 15:12 2024/4/10
 */
public interface IBuffBox {

    long getId();

    long getOwnerId();

    FightContext getFightContext();

    //获取持续时间
    int getLife();

    //减少持续时间
    int reduceLife(int reduce);

    //增加持续时间
    int increaseLife(int increase);

    //创建
    void onCreate();

    //消亡
    void onDestroy(RemoveReason reason);

    //获取buff效果
    Set<IBuffEffect> getBuffEffects();

}
