package com.t13max.data.dao;

import java.util.HashMap;
import java.util.Map;

/**
 * 内存Redis实现 实际上就是个假的 用于微型启动
 *
 * @author: t13max
 * @since: 13:23 2024/5/29
 */
public class MemoryRedisUtil implements IRedisUtil {

    //假装自己是Redis
    private Map<String, String> memoryMap = new HashMap<>();
}
