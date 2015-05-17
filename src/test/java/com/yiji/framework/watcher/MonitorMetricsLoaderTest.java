/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-17 21:16 创建
 *
 */
package com.yiji.framework.watcher;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * @author qiubo@yiji.com
 */
public class MonitorMetricsLoaderTest {
	private MonitorMetricsRepository repository;
	
	@Before
	public void setUp() throws Exception {
		repository = new MonitorMetricsRepository() {
			Set<MonitorMetrics> set = Sets.newHashSet();
			
			@Override
			public void addMonitorMetrics(MonitorMetrics monitorMetrics) {
				set.add(monitorMetrics);
			}
			
			@Override
			public Set<MonitorMetrics> monitorMetricses() {
				return set;
			}
		};
		
	}
	
	@Test
	public void testLoadMetricsFromSPI() throws Exception {
		MonitorMetricsLoader loader = new MonitorMetricsLoader();
		loader.loadMetricsFromSPI(repository);
		Set<MonitorMetrics> set = repository.monitorMetricses();
        Assertions.assertThat(set).hasSize(2);
	}
	
	@Test
	public void testLoadMetricsFromDefaultPackage() throws Exception {
		MonitorMetricsLoader loader = new MonitorMetricsLoader();
		loader.loadMetricsFromDefaultPackage(repository);
		Set<MonitorMetrics> set = repository.monitorMetricses();
		Assertions.assertThat(set).isNotEmpty();
	}
	
}
