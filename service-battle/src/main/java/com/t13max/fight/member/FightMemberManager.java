package com.t13max.fight.member;



import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: t13max
 * @since: 20:00 2024/5/23
 */
/*@Deprecated
public class FightMemberManager extends ManagerBase implements IGameEventListener {

    private Map<Long, IFightMember> memberMap = new ConcurrentHashMap<>();

    private Set<GameEventEnum> interestedEventEnum = Collections.singleton(GameEventEnum.SESSION_CLOSE);

    *//**
     * 获取当前实例对象
     *
     * @Author t13max
     * @Date 16:44 2024/5/23
     *//*
    public static FightMemberManager inst() {
        return ManagerBase.inst(FightMemberManager.class);
    }

    @Override
    public void init() {

    }

    public IFightMember getPlayerMember(long uuid) {
        return memberMap.get(uuid);
    }

    public void putPlayerMember(IFightMember member) {
        this.memberMap.put(member.getId(), member);
    }

    public IFightMember removePlayerMember(long uuid) {
        return this.memberMap.remove(uuid);
    }

    @Override
    public Set<GameEventEnum> getInterestedEvent() {
        return interestedEventEnum;
    }

    @Override
    public void onEvent(IGameEvent gameEvent) {
        switch (gameEvent.getGameEventEnum()) {
            case SESSION_CLOSE -> {
                SessionCLoseEvent sessionCLoseEvent = (SessionCLoseEvent) gameEvent;
                long uuid = sessionCLoseEvent.getUuid();
                //暂时设计成立即删除 后续可优化成5分钟后删除 game则需要入库
                this.removePlayerMember(uuid);
            }
            default -> {

            }
        }
    }

    @Override
    public int getPriority() {
        return 0;
    }

}*/
