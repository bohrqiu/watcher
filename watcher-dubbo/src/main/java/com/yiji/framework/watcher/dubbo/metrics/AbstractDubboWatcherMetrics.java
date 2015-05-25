/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-25 13:54 创建
 *
 */
package com.yiji.framework.watcher.dubbo.metrics;

import com.yiji.framework.watcher.Utils;
import com.yiji.framework.watcher.metrics.base.AbstractCachedWatcherMetrics;

/**
 * @author qiubo@yiji.com
 */
public abstract class AbstractDubboWatcherMetrics extends AbstractCachedWatcherMetrics {
	public AbstractDubboWatcherMetrics() {
		Utils.checkClassExists("com.alibaba.dubbo.common.Version", "dubbo");
	}
}
