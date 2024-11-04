package com.t13max.game.msg;

import lombok.Getter;

/**
 * @author: t13max
 * @since: 15:23 2024/5/29
 */
@Getter
public enum ErrorCode {


    SUCCESS(0),

    FAIL(1),

    ;

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }
}
