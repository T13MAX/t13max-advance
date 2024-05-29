package com.t13max.game.exception;

/**
 * @author: t13max
 * @since: 16:37 2024/4/22
 */
public class DataException extends CommonException{

    public DataException(String message) {
        super(message);
    }

    public DataException(Throwable cause) {
        super(cause);
    }
}
