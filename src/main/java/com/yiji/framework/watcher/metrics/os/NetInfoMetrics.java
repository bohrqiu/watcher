/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-25 22:16 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import com.yiji.framework.watcher.MonitorMetrics;
import com.yiji.framework.watcher.UnsupportMonitorMetricsOperationException;

import java.util.Map;


/**
 * @author qzhanbo@yiji.com
 */
public class NetInfoMetrics implements MonitorMetrics {

    public Object monitor(Map<String, Object> params) {
        try {
            return SigarFactory.getSigar().getNetInfo().toMap();
        } catch (Exception e){
            throw new UnsupportMonitorMetricsOperationException(e);
        }
    }


    public String name() {
        return "netinfo";
    }


    public String desc() {
        return "网络配置情况";
    }
}
