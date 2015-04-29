/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-25 15:53 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author qzhanbo@yiji.com
 */
public class FileDescriptorMetrics extends AbstractMonitorMetrics {
	
	public Object monitor(Map<String, Object> params) {
		OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
		long maxFileDescriptorCount = invoke(os, "getMaxFileDescriptorCount");
		long openFileDescriptorCount = invoke(os, "getOpenFileDescriptorCount");
		HashMap<String, Long> map = Maps.newHashMap();
		map.put("maxFileDescriptorCount", maxFileDescriptorCount);
		map.put("openFileDescriptorCount", openFileDescriptorCount);
		return map;
	}
	
	private long invoke(OperatingSystemMXBean os, String name) {
		try {
			final Method method = os.getClass().getDeclaredMethod(name);
			method.setAccessible(true);
			return (Long) method.invoke(os);
		} catch (Exception e) {
			return -1;
		}
	}
	
	public String name() {
		return "fileDescriptor";
	}
	
	public String desc() {
		return "进程文件描述符使用情况";
	}
}
