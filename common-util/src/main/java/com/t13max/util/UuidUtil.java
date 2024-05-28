package com.t13max.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: t13max
 * @since: 11:10 2024/4/16
 */
@UtilityClass
public class UuidUtil {

    private final AtomicLong ID = new AtomicLong(1000);

    /**
     * 先只保证一次启动唯一后续修改
     *
     * @Author t13max
     * @Date 11:11 2024/4/16
     */
    public long getNextId() {
        return ID.incrementAndGet();
    }

    @Getter
    public static enum FUNCTIONENUM {

        ;
        private int id;


    }

}
