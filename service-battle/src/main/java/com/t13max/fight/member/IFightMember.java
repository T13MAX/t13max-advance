package com.t13max.fight.member;

import battle.entity.FightPlayerInfoPb;
import com.google.protobuf.MessageLite;

/**
 * @author: t13max
 * @since: 17:43 2024/5/28
 */
public interface IFightMember {

    long getId();

    boolean isAttacker();

    void sendMsg(MessageLite messageLite);

    FightPlayerInfoPb buildFightPlayerInfoPb();
}
