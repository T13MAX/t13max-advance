package com.t13max.login.service;


import com.alibaba.fastjson2.JSONObject;
import com.t13max.common.ioc.annotaion.Component;
import com.t13max.data.dao.SqlLiteUtil;
import com.t13max.data.entity.AccountData;
import com.t13max.data.mongo.util.UuidUtil;
import com.t13max.login.verify.IChannelVerify;
import com.t13max.util.limiter.CounterRateLimiter;
import com.t13max.util.limiter.IRateLimiter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: t13max
 * @since: 14:22 2024/5/29
 */
@Component
public class LoginService {

    //限流器
    private final IRateLimiter rateLimiter = new CounterRateLimiter(1500, 1000);

    //验证聚到
    private final Map<String, IChannelVerify> verifyMap = new HashMap<>();


    public LoginService() {

    }

    /**
     * 账号密码登录
     *
     * @Author t13max
     * @Date 14:06 2024/11/5
     */
    public String login(String username, String password) {

        JSONObject result = new JSONObject();
        AccountData accountData = SqlLiteUtil.selectAccount(username);
        if (accountData == null) {
            accountData = new AccountData();
            accountData.setId(UuidUtil.getNextId());
            accountData.setUsername(username);
            accountData.setPassword(password);
            SqlLiteUtil.insertAccount(accountData);
        }
        if (!accountData.getPassword().equals(password)) {
            result.put("result", "error");
            return result.toString();
        }

        return result.toString();
    }


}
