package com.t13max.match.entity;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: t13max
 * @since: 15:54 2024/4/28
 */
@Data
public class MatchServiceInfo {

    //服务名
    private String serviceName;

    //上次更新时间
    private long lastUpdateMills = System.currentTimeMillis();

    //matchEnum列表
    private List<Integer> matchEnumList = new LinkedList<>();
}
