/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-25 15:52 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import java.util.Map;

import com.yiji.framework.watcher.MonitorMetrics;

/**
 * @author qzhanbo@yiji.com
 */
public class SysEnvMetrics implements MonitorMetrics {
	
	public Object monitor(Map<String, Object> params) {
		String key = (String) params.get(KEY);
		if (key == null) {
			return System.getenv();
		} else {
			return System.getenv().get(key);
		}
	}
	
	public String name() {
		return "sysEnv";
	}
	
	public String desc() {
		return "system environment vars. If key=xx is set, the given variable is returned. Otherwise, show all variables.";
	}
}
