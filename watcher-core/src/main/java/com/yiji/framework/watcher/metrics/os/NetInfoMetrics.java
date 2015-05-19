/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-25 22:16 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
public class NetInfoMetrics extends AbstractOSWatcherMetrics {
	
	public Object doMonitor(Map<String, Object> params) throws Exception {
		return SigarFactory.getSigar().getNetInfo().toMap();
	}
	
	@Override
	public CacheTime getCacheTime() {
		return CacheTime.Forver;
	}
	
	public String name() {
		return "netinfo";
	}
	
	public String desc() {
		return "network configuration info";
	}
}
