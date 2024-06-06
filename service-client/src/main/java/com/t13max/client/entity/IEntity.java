package com.t13max.client.entity;

import com.google.protobuf.MessageLite;

/**
 * @author: t13max
 * @since: 20:45 2024/5/30
 */
public interface IEntity {

    void onChange();

    void clear();

    <T extends MessageLite> void update(T t);
}
