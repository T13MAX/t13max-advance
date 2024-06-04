package com.t13max.game.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 配置基类
 *
 * @author: t13max
 * @since: 17:47 2024/5/23
 */
@Setter
@Getter
public class BaseConfig {

    //实例id
    protected String instanceId;

    //实例唯一序号
    protected int instanceNo;

    //是否阻塞主线程
    protected boolean park;

    //netty配置 允许使用默认
    protected NettyConfig netty;

    //业务线程池配置
    protected ActionConfig action;

    //数据库文件
    protected String dbFile;

    protected int saveThreadNum;

    /**
     * 校验数据
     *
     * @Author t13max
     * @Date 18:59 2024/5/23
     */
    public boolean check() {
        return true;
    }
}
