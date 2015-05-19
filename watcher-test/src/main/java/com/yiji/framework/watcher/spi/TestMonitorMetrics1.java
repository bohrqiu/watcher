/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-17 17:52 创建
 *
 */
package com.yiji.framework.watcher.spi;

import java.util.Map;

import com.yiji.framework.watcher.metrics.base.AbstractMonitorMetrics;

/**
 * @author qiubo@yiji.com
 */
public class TestMonitorMetrics1 extends AbstractMonitorMetrics {
	@Override
	public Object monitor(Map<String, Object> params) {
		return "test";
	}
	
	@Override
	public String name() {
		return "test";
	}
	
	@Override
	public String desc() {
		return "test";
	}
}
