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
import com.yiji.framework.watcher.model.Request;
import com.yiji.framework.watcher.model.Result;

/**
 * watcher服务接口
 * @author qiubo@yiji.com
 */
public interface WatcherService extends ExtensionRepository<WatcherMetrics> {
	/**
	 * 通过请求获取到watcher结果数据
	 * @param request 请求
	 * @return 结果
	 */
    Result watch(Request request);
}
