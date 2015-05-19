/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-24 23:48 创建
 *
 */
package com.yiji.framework.watcher;

import com.yiji.framework.watcher.extension.ExtensionRepository;

/**
 * 监控服务接口
 * @author qiubo@yiji.com
 */
public interface MonitorService extends ExtensionRepository<MonitorMetrics> {
	/**
	 * 通过监控请求获取到监控结果数据
	 * @param request 监控请求
	 * @return 监控结果
	 */
	String monitor(MonitorRequest request);
}
