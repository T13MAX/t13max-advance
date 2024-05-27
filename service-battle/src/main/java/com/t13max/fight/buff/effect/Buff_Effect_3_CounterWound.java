package com.t13max.fight.buff.effect;

import lombok.Getter;

/**
 * 反伤
 *
 * @author: t13max
 * @since: 15:19 2024/5/24
 */
@Getter
public class Buff_Effect_3_CounterWound extends AbstractEffect {

    //反伤的比例
    private int rate;

    @Override
    protected void onInit() {
        this.rate = Integer.parseInt(this.param);
    }
}
