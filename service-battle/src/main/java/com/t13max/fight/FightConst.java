package com.t13max.fight;

import com.t13max.util.TimeUtil;

/**
 * @author: t13max
 * @since: 11:49 2024/5/28
 */
public interface FightConst {

    //行动超时时间
    int ACTION_TIMEOUT_MILLS = 60 * TimeUtil.SECOND;

    //等待进入超时时间
    int WAIT_JOIN_TIMEOUT_MILLS = 60 * TimeUtil.SECOND;
}
