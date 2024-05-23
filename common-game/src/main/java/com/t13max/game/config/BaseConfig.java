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

    //是否阻塞主线程
    protected boolean park;

    //netty配置
    protected NettyConfig netty;


    /**
     * 校验数据
     *
     * @Author t13max
     * @Date 18:59 2024/5/23
     */
    public boolean check() {
        return false;
    }
}
