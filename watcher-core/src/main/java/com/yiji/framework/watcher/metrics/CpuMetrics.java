/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-25 15:46 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Map;

import com.google.common.collect.Maps;
import com.yiji.framework.watcher.metrics.base.AbstractCachedWatcherMetrics;

/**
 * @author qiubo@yiji.com
 */
public class CpuMetrics extends AbstractCachedWatcherMetrics {
	
	public Object doMonitor(Map<String, Object> params) {
		Map<String, Object> map = Maps.newHashMap();
		OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();
		map.put("processors", Runtime.getRuntime().availableProcessors());
		map.put("systemLoadAverage", mxBean.getSystemLoadAverage());
		return map;
	}
	
	@Override
	public CacheTime getCacheTime() {
		return CacheTime.THIRTY_SECOND;
	}
	
	public String name() {
		return "cpu";
	}
	
	public String desc() {
		return "show processor info and system load";
	}
}
