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

import java.util.List;
import java.util.Map;

import org.hyperic.sigar.CpuInfo;

import com.google.common.collect.Lists;

/**
 * @author qiubo@yiji.com
 */
public class CpuInfoMetrics extends AbstractOSMonitorMetrics {
	
	public Object doMonitor(Map<String, Object> params) throws Exception {
		CpuInfo[] cpuInfos = SigarFactory.getSigar().getCpuInfoList();
		List<Map> result = Lists.newArrayList();
		if (cpuInfos != null) {
			for (CpuInfo cpuInfo : cpuInfos) {
				result.add(cpuInfo.toMap());
			}
		}
		return result;
	}
	
	@Override
	public CacheTime getCacheTime() {
		return CacheTime.Forver;
	}
	
	public String name() {
		return "cpuinfo";
	}
	
	public String desc() {
		return "cpu details";
	}
}
