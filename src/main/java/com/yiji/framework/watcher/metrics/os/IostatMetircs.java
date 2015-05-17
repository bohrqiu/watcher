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

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * @author qiubo@yiji.com
 */
public class IostatMetircs extends AbstractOSMonitorMetrics {
	private static final Logger logger = LoggerFactory.getLogger(IostatMetircs.class);
	
	@Override
	public Object doMonitor(Map<String, Object> params) throws Exception {
		
		List<String> result = Lists.newArrayList();
		
		WatcherIostat iostat = new WatcherIostat();
		iostat.processCommand(new String[0]);
		result = iostat.getResult();
		return result;
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
