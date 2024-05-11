package com.t13max.util;

import lombok.experimental.UtilityClass;

/**
 * @author: t13max
 * @since: 11:50 2024/4/11
 */
@UtilityClass
public class TimeUtil {

    public long nowMills() {
        return System.currentTimeMillis();
    }
}
