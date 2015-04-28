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

import com.yiji.framework.watcher.MonitorMetrics;

import java.util.Map;


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
		return "环境变量,不带任何参数，返回所有配置，传入参数key=xx，返回指定的参数.";
	}
}
