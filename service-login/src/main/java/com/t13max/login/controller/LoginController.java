package com.t13max.login.controller;

import com.t13max.common.ioc.annotaion.Autowired;
import com.t13max.common.ioc.annotaion.Component;
import com.t13max.common.ioc.annotaion.Request;
import com.t13max.login.consts.Const;
import com.t13max.login.service.LoginService;

import java.util.Map;

/**
 * @author: t13max
 * @since: 14:22 2024/5/29
 */
@Component
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Request("/login")
    public String login(Map<String, String> paramMap) {
        String username = paramMap.get(Const.USERNAME);
        String password = paramMap.get(Const.PASSWORD);
        return loginService.login(username, password);
    }
}
