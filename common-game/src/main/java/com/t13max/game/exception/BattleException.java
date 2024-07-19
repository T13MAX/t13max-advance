package com.t13max.game.exception;

/**
 * @author: t13max
 * @since: 18:09 2024/7/19
 */
public class BattleException extends RuntimeException{

    public BattleException(String message) {
        super(message);
    }

    public BattleException(String message, Throwable cause) {
        super(message, cause);
    }

    public BattleException(Throwable cause) {
        super(cause);
    }
}
