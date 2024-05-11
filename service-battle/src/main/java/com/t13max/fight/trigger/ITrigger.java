package com.t13max.fight.trigger;

/**
 * @author: t13max
 * @since: 17:01 2024/4/15
 */
public interface ITrigger {

    void onTimeIsUp();

    int getDelayTime();

}
