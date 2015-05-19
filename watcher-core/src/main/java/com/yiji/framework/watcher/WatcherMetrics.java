/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-24 23:52 创建
 *
 */
package com.yiji.framework.watcher;

import java.util.Map;

/**
 * 监控指标
 * @author qiubo@yiji.com
 */
public interface WatcherMetrics extends MetricsName, Comparable<WatcherMetrics> {

    /**
	 * 通过请求参数返回监控结果对象
	 */
	Object watch(Map<String, Object> params);
	
}
