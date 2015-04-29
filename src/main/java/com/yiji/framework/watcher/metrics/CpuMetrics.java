/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-25 15:46 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author qzhanbo@yiji.com
 */
public class CpuMetrics extends AbstractMonitorMetrics {
	
	public Object monitor(Map<String, Object> params) {
		Map<String, Object> map = Maps.newHashMap();
		OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();
		map.put("processors", Runtime.getRuntime().availableProcessors());
		map.put("systemLoadAverage", mxBean.getSystemLoadAverage());
		return map;
	}
	
	public String name() {
		return "cpu";
	}
	
	public String desc() {
		return "cpu概况";
	}
}
