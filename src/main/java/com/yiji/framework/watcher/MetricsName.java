/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-25 15:29 创建
 *
 */
package com.yiji.framework.watcher;

/**
 * @author qzhanbo@yiji.com
 */
public interface MetricsName {
	/**
	 * 监控指标名称
	 * @return
	 */
	String name();
	
	/**
	 * 监控指标描述
	 * @return
	 */
	String desc();
}
