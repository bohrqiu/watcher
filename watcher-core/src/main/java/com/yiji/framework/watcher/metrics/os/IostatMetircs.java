/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-04 18:33 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qiubo@yiji.com
 */
public class IostatMetircs extends AbstractOSWatcherMetrics {
	private static final Logger logger = LoggerFactory.getLogger(IostatMetircs.class);
	
	@Override
	public Object doMonitor(Map<String, Object> params) throws Exception {
		
		List<String> result;
		
		WatcherIostat iostat = new WatcherIostat();
		iostat.processCommand(new String[0]);
		result = iostat.getResult();
		return result;
	}
	
	@Override
	public CacheTime getCacheTime() {
		return CacheTime.FIVE_SECOND;
	}
	
	@Override
	public String name() {
		return "iostat";
	}
	
	@Override
	public String desc() {
		return "io usage";
	}
}
