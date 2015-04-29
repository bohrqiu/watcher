/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-25 15:47 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import java.lang.management.ManagementFactory;
import java.util.Map;

import com.yiji.framework.watcher.MonitorMetrics;

/**
 * @author qzhanbo@yiji.com
 */
public class PidMetrics implements MonitorMetrics {
	private Long pid;
	
	public Object monitor(Map<String, Object> params) {
		return getProcessId();
	}
	
	private Long getProcessId() {
		if (pid != null) {
			return pid;
		}
		final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
		final int index = jvmName.indexOf('@');
		
		if (index < 1) {
			return -1l;
		}
		try {
			pid = Long.parseLong(jvmName.substring(0, index));
			return pid;
		} catch (NumberFormatException e) {
			// ignore
		}
		return -1l;
	}
	
	public String name() {
		return "pid";
	}
	
	public String desc() {
		return "process id";
	}
}
