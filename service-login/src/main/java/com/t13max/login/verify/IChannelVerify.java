package com.t13max.login.verify;

/**
 * 渠道校验接口
 *
 * @author t13max
 * @since 15:12 2024/11/5
 */
public interface IChannelVerify {

    String[] verify(String token, String uid) throws Exception;
}
