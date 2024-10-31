package com.t13max.game.memory;

import com.google.protobuf.MessageLite;
import com.t13max.data.mongo.IData;
import com.t13max.game.player.Player;

/**
 * @author: t13max
 * @since: 18:59 2024/6/4
 */
public abstract class AbstractMemory<PB extends MessageLite, DATA extends IData> implements IMemory<PB, DATA> {

    protected Player player;

    public void setOwner(Player player) {
        this.player = player;
    }

}
