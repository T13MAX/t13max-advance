package com.t13max.data.entity;

import lombok.Data;

/**
 * @author: t13max
 * @since: 11:30 2024/5/29
 */
@Data
public class AccountData {

    private long id;

    private String username;

    //md5加密存储?
    private String password;
}
