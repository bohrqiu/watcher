/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-25 21:04 创建
 *
 */
package com.yiji.framework.watcher.extension;

import java.util.Set;

/**
 * 扩展仓储
 * @author qiubo@yiji.com
 */
public interface ExtensionRepository<T> {
	
	/**
	 * 添加扩展
	 */
	void add(T t);
	
	/**
	 * 获取所有扩展
	 */
	Set<T> set();
}
