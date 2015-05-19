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
public class OperationException extends RuntimeException {
	public OperationException() {
		super();
	}
	
	public OperationException(Throwable cause) {
		super(cause);
	}
	
	public OperationException(String message) {
		super(message);
	}
	
	public OperationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
	
	public static OperationException throwIt(String message) {
		throw new OperationException(message);
	}
	
	public static OperationException throwIt(Throwable cause) {
		throw new OperationException(cause);
	}
	
	public static OperationException throwIt(String message, Throwable cause) {
		throw new OperationException(message, cause);
	}
}
