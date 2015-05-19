/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-25 15:48 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.Map;

import com.google.common.collect.Maps;
import com.yiji.framework.watcher.metrics.base.AbstractCachedMonitorMetrics;

/**
 * @author qiubo@yiji.com
 */
public class JvmMemMetrics extends AbstractCachedMonitorMetrics {
	
	public Object doMonitor(Map<String, Object> params) {
		Map<String, Object> map = Maps.newHashMap();
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		map.put("heapMemoryUsage", memoryMXBean.getHeapMemoryUsage());
		map.put("nonHeapMemoryUsage", memoryMXBean.getNonHeapMemoryUsage());
		map.put("totalMemory", Runtime.getRuntime().totalMemory() / 1024 / 1024 + "m");
		map.put("freeMemory", Runtime.getRuntime().freeMemory() / 1024 / 1024 + "m");
		map.put("maxMemory", Runtime.getRuntime().maxMemory() / 1024 / 1024 + "m");
		return map;
	}
	
	@Override
	public CacheTime getCacheTime() {
		return CacheTime.THIRTY_SECOND;
	}
	
	public String name() {
		return "jvmMem";
	}
	
	public String desc() {
		return "jvm memory use stats";
	}
}
