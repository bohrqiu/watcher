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

import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qiubo@yiji.com
 */
public class OsVersionMetircs extends AbstractOSMonitorMetrics {
	private static final Logger logger = LoggerFactory.getLogger(OsVersionMetircs.class);
	
	@Override
	public Object doMonitor(Map<String, Object> params) throws SigarException {
		return OSVersion.getResult();
	}
	
	@Override
	public String name() {
		return "osVersion";
	}
	
	@Override
	public String desc() {
		return "os version info";
	}
}
