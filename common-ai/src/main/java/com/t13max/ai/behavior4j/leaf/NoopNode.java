package com.t13max.ai.behavior4j.leaf;


import com.t13max.ai.behavior4j.LeafNode;

/**
 * @auther: zhoupeng
 * Date: 2020/2/14 15:30
 * @Description: node do nothing
 */
public class NoopNode<E> extends LeafNode<E> {

    public Status execute() {
        return Status.BT_SUCCESS;
    }
}
