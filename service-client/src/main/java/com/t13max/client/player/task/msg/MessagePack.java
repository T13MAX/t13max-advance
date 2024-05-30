package com.t13max.client.player.task.msg;

import com.google.protobuf.MessageLite;
import lombok.Data;

/**
 * @author: t13max
 * @since: 14:22 2024/5/30
 */
@Data
public class MessagePack<T extends MessageLite> {

    private T message;

    private int resCode;

    public MessagePack(int resCode) {
        this.resCode = resCode;
    }

    public MessagePack(T message) {
        this.message = message;
    }

    public boolean isError() {
        return resCode != 0;
    }

    public boolean isSuccess() {
        return resCode == 0;
    }
}
