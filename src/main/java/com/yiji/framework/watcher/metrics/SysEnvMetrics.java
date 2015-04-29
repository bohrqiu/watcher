/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-25 15:52 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
public class SysEnvMetrics extends AbstractMonitorMetrics {
	
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
		return "system environment vars. Optional parameter: key=xx";
	}
}
