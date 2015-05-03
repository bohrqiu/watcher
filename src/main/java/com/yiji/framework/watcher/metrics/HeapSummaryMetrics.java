/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-30 16:00 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import java.util.Map;

import sun.jvm.hotspot.tools.HeapSummary;

import com.yiji.framework.watcher.Utils;

/**
 * @author qiubo@yiji.com
 */
public class HeapSummaryMetrics extends AbstractMonitorMetrics {
	@Override
	public Object monitor(Map<String, Object> params) {
		HeapSummary hs = new HeapSummary();
		hs.main(new String[] { Utils.getPid().toString() });
		return null;
	}
	
	@Override
	public String name() {
		return "heapSummary";
	}
	
	@Override
	public String desc() {
		return "HeapSummary";
	}
}
