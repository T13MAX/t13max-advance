package com.t13max.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * @author: t13max
 * @since: 14:23 2024/5/23
 */
public class Log {
    public static Logger common = LogManager.getLogger("COMMON");
    public static Logger template = LogManager.getLogger("TEMPLATE");
    public static Logger game = LogManager.getLogger("GAME");
    public static Logger battle = LogManager.getLogger("BATTLE");
    public static Logger scene = LogManager.getLogger("SCENE");
}
