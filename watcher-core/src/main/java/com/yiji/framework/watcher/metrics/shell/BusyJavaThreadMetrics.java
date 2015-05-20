/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-15 18:58 创建
 *
 */
package com.yiji.framework.watcher.metrics.shell;

import static com.yiji.framework.watcher.Utils.getParam;
import static com.yiji.framework.watcher.Utils.getPid;

import java.util.Map;

import com.yiji.framework.watcher.WatcherException;
import com.yiji.framework.watcher.Utils;

/**
 * @author qiubo@yiji.com
 */
public class BusyJavaThreadMetrics extends AbstractShellWatcherMetrics {
	@Override
	public Object doMonitor(Map<String, Object> params) throws Throwable {
		if (!Utils.isLinux()) {
            throw WatcherException.throwIt("此脚本仅支持linux");
		}
		Object count = getParam(params, "count", "5");
		return shellExecutor.exeShell("show-busy-java-threads.sh", "-p " + getPid(), "-c " + count);
	}
	
	@Override
	public CacheTime getCacheTime() {
		return CacheTime.THIRTY_SECOND;
	}
	
	@Override
	public String name() {
		return "busyJavaThread";
	}
	
	@Override
	public String desc() {
		return "show busy java thread,Optional parameters:count=10,default count=5";
	}
}
