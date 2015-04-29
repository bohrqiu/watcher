/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-25 15:55 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author qiubo@yiji.com
 */
public class ClassloadMetrics extends AbstractMonitorMetrics {
	
	public Object monitor(Map<String, Object> params) {
		Map<String, Object> map = Maps.newHashMap();
		ClassLoadingMXBean mxBean = ManagementFactory.getClassLoadingMXBean();
		map.put("totalLoadedClassCount", mxBean.getTotalLoadedClassCount());
		map.put("loadedClassCount", mxBean.getLoadedClassCount());
		map.put("unloadedClassCount", mxBean.getUnloadedClassCount());
		return map;
		
	}
	
	public String name() {
		return "classload";
	}
	
	public String desc() {
		return "show jvm classload stats";
	}
}
