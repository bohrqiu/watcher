/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-25 15:47 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import java.util.Map;

import com.yiji.framework.watcher.Utils;
import com.yiji.framework.watcher.metrics.base.AbstractMonitorMetrics;

/**
 * @author qiubo@yiji.com
 */
public class PidMetrics extends AbstractMonitorMetrics {
	public Object monitor(Map<String, Object> params) {
		return getProcessId();
	}
	
	private Long getProcessId() {
		
		return Utils.getPid();
	}
	
	public String name() {
		return "pid";
	}
	
	public String desc() {
		return "process id";
	}
}
