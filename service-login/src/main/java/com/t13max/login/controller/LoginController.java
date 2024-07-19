package com.t13max.login.controller;

import com.t13max.common.ioc.annotaion.Autowired;
import com.t13max.common.ioc.annotaion.Component;
import com.t13max.login.service.LoginService;

/**
 * @author: t13max
 * @since: 14:22 2024/5/29
 */
@Component
public class LoginController {

    @Autowired
    private LoginService loginService;

    public void login() {
        loginService.login();
    }
}
