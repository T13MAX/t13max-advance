package com.t13max.game.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 日志工具类
 *
 * @author: t13max
 * @since: 18:21 2024/7/19
 */
public class Log {
    public static Logger def = LogManager.getLogger("DEF");
    public static Logger msg = LogManager.getLogger("MSG");
    public static Logger scene = LogManager.getLogger("SCENE");
    public static Logger game = LogManager.getLogger("GAME");
    public static Logger battle = LogManager.getLogger("BATTLE");
    public static Logger template = LogManager.getLogger("TEMPLATE");
    public static Logger data = LogManager.getLogger("DATA");
    public static Logger client = LogManager.getLogger("CLIENT");
}
