/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-25 22:21 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import java.util.List;
import java.util.Map;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.SigarException;

import com.google.common.collect.Lists;

/**
 * @author qzhanbo@yiji.com
 */
public class CpuInfoMetrics extends AbstractOSMonitorMetrics {
	private List<Map> result;
	
	public Object doMonitor(Map<String, Object> params) throws SigarException {
		if (result != null) {
			return result;
		}
		CpuInfo[] cpuInfos = SigarFactory.getSigar().getCpuInfoList();
		List<Map> temp = Lists.newArrayList();
		if (cpuInfos != null) {
			for (CpuInfo cpuInfo : cpuInfos) {
				temp.add(cpuInfo.toMap());
			}
		}
		this.result = temp;
		return this.result;
	}
	
	public String name() {
		return "cpuinfo";
	}
	
	public String desc() {
		return "cpu详细信息";
	}
}
