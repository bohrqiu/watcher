/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-25 22:21 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
public class NetStatMetrics extends AbstractOSWatcherMetrics {
	
	public Object doMonitor(Map<String, Object> params) throws Exception {
		return SigarFactory.getSigar().getNetStat();
	}
	
	@Override
	public CacheTime getCacheTime() {
		return CacheTime.FIVE_SECOND;
	}
	
	public String name() {
		return "netstat";
	}
	
	public String desc() {
		return "network use stats";
	}
}
