package com.t13max.match.consts;

/**
 * @author t13max
 * @since 16:37 2024/11/4
 */
public interface Const {
    //首次延迟
    int DELAY = 5;
    //匹配间隔
    int PERIOD = 1;
    //分配时间
    int DISTRIBUTE_INTERVAL = 10 * 1000;
    //主存在冗余时间 保证主一直是主
    int DISTRIBUTE_INTERVAL_REDUNDANCY = 5 * 1000;
    //直接对战机器人的超时时间特殊值
    long DIRECT_ROBOT_SPECIAL_VALUE = -1L;
    //数据超时
    int DATA_TIME_OUT = 3 * 60 * 1000;
    //服务处理超时
    int SERVICE_TIME_OUT = 30 * 1000;
    String DISTRIBUTE_TIME = "DISTRIBUTE_TIME";
    String SERVICE_KEY = "SERVICE_KEY";
    String DIMENSION = "DIMENSION";
    String MATCH_TIMEOUT = "MATCH_TIMEOUT";
    String MAIN_SERVICE_NAME = "MAIN_SERVICE_NAME";
    String MATCH_PLAYER_INFO = "MATCH_PLAYER_INFO";
    String MATCH_SET = "MATCH_SET";
}
