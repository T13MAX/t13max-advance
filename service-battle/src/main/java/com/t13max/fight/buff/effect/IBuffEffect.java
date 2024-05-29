package com.t13max.fight.buff.effect;


import battle.event.entity.BuffStatus;
import battle.event.entity.RemoveReason;
import com.t13max.fight.buff.IBuffBox;

/**
 * @author: t13max
 * @since: 16:11 2024/4/10
 */
public interface IBuffEffect {

    //获取持续时间
    int getLife();

    //减少持续时间
    int reduceLife(int reduce);

    //增加持续时间
    int increaseLife(int increase);

    //获取buff状态
    BuffStatus getBuffStatus();

    //获取所属buff盒子
    IBuffBox getBuffBox();

    //创建
    void onCreate();

    //消亡
    void onDestroy(RemoveReason reason);

}
