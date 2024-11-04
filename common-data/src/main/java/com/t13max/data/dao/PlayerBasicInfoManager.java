package com.t13max.data.dao;

import com.t13max.common.manager.ManagerBase;
import com.t13max.game.api.args.PlayerBasicInfo;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author t13max
 * @since 15:57 2024/11/4
 */
public class PlayerBasicInfoManager extends ManagerBase {

    public static PlayerBasicInfoManager inst() {
        return inst(PlayerBasicInfoManager.class);
    }


    /**
     * 获取玩家简要信息
     * 理论上 他依赖Redis 没有则查库
     *
     * @Author t13max
     * @Date 15:59 2024/11/4
     */
    public Map<Long, PlayerBasicInfo> searchPlayerBasicInfo(Set<Long> singleton) {
        return Collections.emptyMap();
    }
}
