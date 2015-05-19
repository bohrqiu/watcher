/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-20 00:53 创建
 *
 */
package com.yiji.framework.watcher;

import com.yiji.framework.watcher.model.Request;

/**
 * @author qiubo@yiji.com
 */
public interface MarshalledWatcherService extends Marshaller, WatcherService {
	String watchAndMarshall(Request request);
}
