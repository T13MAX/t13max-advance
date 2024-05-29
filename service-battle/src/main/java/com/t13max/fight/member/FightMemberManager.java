package com.t13max.fight.member;

import com.t13max.game.manager.ManagerBase;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: t13max
 * @since: 20:00 2024/5/23
 */
public class FightMemberManager extends ManagerBase {

    private Map<Long, IFightMember> memberMap = new ConcurrentHashMap<>();

    /**
     * 获取当前实例对象
     *
     * @Author t13max
     * @Date 16:44 2024/5/23
     */
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
}
