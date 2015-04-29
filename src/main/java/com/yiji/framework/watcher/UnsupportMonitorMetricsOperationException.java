/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-25 15:17 创建
 *
 */
package com.yiji.framework.watcher;

/**
 * @author qiubo@yiji.com
 */
public class UnsupportMonitorMetricsOperationException extends RuntimeException {
	public UnsupportMonitorMetricsOperationException() {
		super();
	}
	
	public UnsupportMonitorMetricsOperationException(Throwable cause) {
		super(cause);
	}
	
	public UnsupportMonitorMetricsOperationException(String message) {
		super(message);
	}
	
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
