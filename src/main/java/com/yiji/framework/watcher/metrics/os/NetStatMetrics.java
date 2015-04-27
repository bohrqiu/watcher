/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-25 22:21 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import com.yiji.framework.watcher.MonitorMetrics;
import com.yiji.framework.watcher.UnsupportMonitorMetricsOperationException;

import java.util.Map;


/**
 * @author qzhanbo@yiji.com
 */
public class NetStatMetrics implements MonitorMetrics {

    public Object monitor(Map<String, Object> params) {
        try {
            return SigarFactory.getSigar().getNetStat();
        } catch (Exception e){
            throw new UnsupportMonitorMetricsOperationException(e);
        }

    }


    public String name() {
        return "netstat";
    }

    public String desc() {
        return "网络使用情况统计";
    }
}
