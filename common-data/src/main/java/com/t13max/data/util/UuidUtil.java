package com.t13max.data.util;

import com.t13max.game.config.BaseConfig;
import com.t13max.game.run.Application;
import com.t13max.util.Log;
import lombok.experimental.UtilityClass;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: t13max
 * @since: 13:57 2024/5/29
 */
@UtilityClass
public class UuidUtil {
    private static long START_TIME = 1716912000000L;//2024-05-29 00:00:00

    private final AtomicLong ID = new AtomicLong();

    private static int MASK_12 = 0XFFF;//12位掩码

    private static int INSTANCE_NO;

    static {
        BaseConfig config = Application.config();
        if (config != null) {
            INSTANCE_NO = Application.config().getInstanceNo();
        } else {
            Log.data.error("config为空, instanceNo为0");
        }
    }

    /**
     * 全局唯一
     *
     * @Author t13max
     * @Date 11:11 2024/4/16
     */
    public long getNextId() {
        long currentTime = System.currentTimeMillis() - START_TIME;
        return (currentTime << 23) + (INSTANCE_NO << 12) + (ID.getAndIncrement() & MASK_12);
    }

}
