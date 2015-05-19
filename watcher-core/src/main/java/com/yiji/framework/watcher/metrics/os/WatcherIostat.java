/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-04 18:32 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import java.util.List;

import org.hyperic.sigar.cmd.Iostat;

import com.google.common.collect.Lists;

/**
 * @author qiubo@yiji.com
 */
public class WatcherIostat extends Iostat {
	private List<String> result = Lists.newArrayList();
	
	@Override
	public void println(String line) {
		result.add(line);
	}
	
	public List<String> getResult() {
		return result;
	}
}
