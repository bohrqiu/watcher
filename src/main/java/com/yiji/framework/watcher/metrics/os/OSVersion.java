/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-04 19:44 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import java.util.Map;

import org.hyperic.sigar.OperatingSystem;

import com.google.common.collect.Maps;

/**
 * @author qiubo@yiji.com
 */
public class OSVersion {
	private static Map<String, String> result = Maps.newHashMap();
	
	public static Map<String, String> getResult() {
		if (result.isEmpty()) {
			result.put("Current user", System.getProperty("user.name"));
			OperatingSystem sys = OperatingSystem.getInstance();
			result.put("OS description", sys.getDescription());
			result.put("OS name", sys.getName());
			result.put("OS arch", sys.getArch());
			result.put("OS machine", sys.getMachine());
			result.put("OS version", sys.getVersion());
			result.put("OS description", sys.getDescription());
			result.put("OS patch level", sys.getPatchLevel());
			
			result.put("OS vendor", sys.getVendor());
			result.put("OS vendor version", sys.getVendorVersion());
			if (sys.getVendorCodeName() != null) {
				result.put("OS code name", sys.getVendorCodeName());
			}
			result.put("OS data model", sys.getDataModel());
			result.put("OS cpu endian", sys.getCpuEndian());
			result.put("Java vm version", System.getProperty("java.vm.version"));
			result.put("Java vm vendor", System.getProperty("java.vm.vendor"));
			result.put("Java home", System.getProperty("java.home"));
			return result;
		} else {
			return result;
		}
	}
	
}
