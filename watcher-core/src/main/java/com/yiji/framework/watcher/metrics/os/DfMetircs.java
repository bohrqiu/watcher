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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qiubo@yiji.com
 */
public class DfMetircs extends AbstractOSWatcherMetrics {
	private static final Logger logger = LoggerFactory.getLogger(DfMetircs.class);
	
	@Override
	public Object doMonitor(Map<String, Object> params) throws Exception {
		WatcherDf iostat = new WatcherDf();
		iostat.processCommand(new String[0]);
		return iostat.getResult();
	}
	
	@Override
	public CacheTime getCacheTime() {
		return CacheTime.THIRTY_SECOND;
	}
	
	@Override
	public String name() {
		return "df";
	}
	
	@Override
	public String desc() {
		return "filesystem disk space usage";
	}
}
