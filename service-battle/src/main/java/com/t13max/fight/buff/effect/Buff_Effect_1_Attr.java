package com.t13max.fight.buff.effect;

import com.t13max.fight.buff.RemoveReason;

/**
 * 属性改变效果
 *
 * @author: t13max
 * @since: 16:09 2024/4/10
 */
public class Buff_Effect_1_Attr extends AbstractEffect {

    @Override
    public void onCreate() {
        super.onCreate();
        this.buffBox.getOwner().getFightAttrManager().modifyAttr( this.param, true);
    }

    @Override
    public void onDestroy(RemoveReason reason) {
        super.onDestroy(reason);
        this.buffBox.getOwner().getFightAttrManager().modifyAttr( this.param, false);
    }
}
