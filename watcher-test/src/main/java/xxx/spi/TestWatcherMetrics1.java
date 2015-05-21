/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-17 17:52 创建
 *
 */
package xxx.spi;

import java.util.Map;

import com.codahale.metrics.Timer;
import com.yiji.framework.watcher.MetricsHolder;
import com.yiji.framework.watcher.metrics.base.AbstractWatcherMetrics;

/**
 * @author qiubo@yiji.com
 */
public class TestWatcherMetrics1 extends AbstractWatcherMetrics {
	public TestWatcherMetrics1() {
		Timer requests = MetricsHolder.metricRegistry().timer("requests");
		requests.time();

    }
	
	@Override
	public Object watch(Map<String, Object> params) {
		return TestWatcherMetrics1.class.getName();
	}
	
	@Override
	public String name() {
		return "test";
	}
	
	@Override
	public String desc() {
		return "test";
	}
}
