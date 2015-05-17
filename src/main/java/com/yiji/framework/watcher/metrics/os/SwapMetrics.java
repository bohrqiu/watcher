/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-25 22:21 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
public class SwapMetrics extends AbstractOSMonitorMetrics {
	
	public Object doMonitor(Map<String, Object> params) throws Exception {
        return SigarFactory.getSigar().getSwap().toMap();
	}

    @Override
    public CacheTime getCacheTime() {
        return new CacheTime.Time(5 * 1000);
    }
	public String name() {
		return "swap";
	}
	
	public String desc() {
		return "os swap use stats";
	}
}
