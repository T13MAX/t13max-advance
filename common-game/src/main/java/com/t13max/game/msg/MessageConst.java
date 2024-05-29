package com.t13max.game.msg;


/**
 * @author: t13max
 * @since: 20:08 2024/5/28
 */
public interface MessageConst {
    public static final int LEN_LENGTH = 4;
    public static final int MSG_ID_LENGTH = 4;
    public static final int MSG_RESULT_COE = 4;
    public static final int HEADER_LENGTH = LEN_LENGTH + MSG_ID_LENGTH;
    public static final int CLIENT_HEADER_LENGTH = LEN_LENGTH + MSG_ID_LENGTH + MSG_RESULT_COE;
}
