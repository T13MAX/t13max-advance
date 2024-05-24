package com.t13max.fight.msg;

import com.google.protobuf.MessageLite;
import com.t13max.fight.FightMatch;
import com.t13max.fight.MatchManager;
import com.t13max.game.msg.IMessage;
import com.t13max.game.msg.ISession;

/**
 * @author: t13max
 * @since: 11:10 2024/5/24
 */
public abstract class AbstractMessage<T extends MessageLite> implements IMessage<T> {

}
