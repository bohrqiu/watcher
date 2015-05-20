/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-20 14:35 创建
 *
 */
package com.yiji.framework.watcher.dubbo;

import com.yiji.framework.watcher.WatcherDependencyNotFoundException;

/**
 * @author qiubo@yiji.com
 */
public class DubboDependencyChecker {
	public void check() {
		try {
			Thread.currentThread().getContextClassLoader().loadClass("com.alibaba.dubbo.registry.support.AbstractRegistryFactory");
		} catch (ClassNotFoundException e) {
			throw new WatcherDependencyNotFoundException("dubbo dependency is not found");
		}
	}
}
