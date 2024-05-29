package com.t13max.game.msg;

import com.google.protobuf.MessageLite;
import com.t13max.game.player.JobName;
import com.t13max.game.player.Player;
import com.t13max.game.player.PlayerManager;
import com.t13max.game.run.Application;
import com.t13max.game.session.ISession;

/**
 * @author: t13max
 * @since: 14:40 2024/5/23
 */
public abstract class AbstractMessage<T extends MessageLite> implements IMessage<T> {

    @Override
    public void doMessage(ISession session, int msgId, T message) {
        Player player = PlayerManager.inst().getPlayer(session.getUuid());
        if (player == null) {
            Application.autoLogger().error("玩家不存在, 无法处理消息, uuid={}, message={}", session.getUuid(), message);
            return;
        }
        //在自己的业务线程池中执行
        player.execute(JobName.DEF, () -> doMessage(player, message));
    }

    public abstract void doMessage(Player player, T message);

}
