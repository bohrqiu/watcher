/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-24 23:48 创建
 *
 */
package com.yiji.framework.watcher;

/**
 * @author qzhanbo@yiji.com
 */
public interface MonitorService extends MonitorMetricsRepository {
	
	String monitor(MonitorRequest request);
}
