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
public class MonitorMetricsOperationException extends RuntimeException {
	public MonitorMetricsOperationException() {
		super();
	}
	
	public MonitorMetricsOperationException(Throwable cause) {
		super(cause);
	}
	
	public MonitorMetricsOperationException(String message) {
		super(message);
	}

    public MonitorMetricsOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
