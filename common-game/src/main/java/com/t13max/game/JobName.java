package com.t13max.game;

import com.t13max.common.action.IJobName;

/**
 * @author: t13max
 * @since: 18:08 2024/7/19
 */
public enum JobName implements IJobName {

    XXX("XXX");;

    JobName(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public String getJobName() {
        return this.name;
    }
}
