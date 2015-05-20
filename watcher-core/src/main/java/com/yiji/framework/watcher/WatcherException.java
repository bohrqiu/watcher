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
public class WatcherException extends RuntimeException {
	public WatcherException() {
		super();
	}
	
	public WatcherException(Throwable cause) {
		super(cause);
	}
	
	public WatcherException(String message) {
		super(message);
	}
	
	public WatcherException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
	
	public static WatcherException throwIt(String message) {
		throw new WatcherException(message);
	}
	
	public static WatcherException throwIt(Throwable cause) {
		throw new WatcherException(cause);
	}
	
	public static WatcherException throwIt(String message, Throwable cause) {
		throw new WatcherException(message, cause);
	}
}
