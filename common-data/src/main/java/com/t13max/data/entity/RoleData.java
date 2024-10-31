package com.t13max.data.entity;

import com.t13max.data.mongo.IData;
import lombok.Data;

/**
 * @author: t13max
 * @since: 11:05 2024/5/24
 */
@Data
public class RoleData implements IData {

    //所属玩家的唯一id
    private long uuid;

    //唯一id
    private long roleId;

    //解锁的功能模块
    private long unlockMemory;


    @Override
    public long getId() {
        return uuid;
    }
}
