package com.t13max.game.exception;

/**
 * @author: t13max
 * @since: 18:09 2024/7/19
 */
public class GameException extends RuntimeException{

    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameException(Throwable cause) {
        super(cause);
    }
}
