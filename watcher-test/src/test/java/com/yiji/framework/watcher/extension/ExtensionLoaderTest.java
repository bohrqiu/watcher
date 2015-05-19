/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-17 21:16 创建
 *
 */
package com.yiji.framework.watcher.extension;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.yiji.framework.watcher.MonitorMetrics;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author qiubo@yiji.com
 */
public class ExtensionLoaderTest {
	private ExtensionRepository<MonitorMetrics> repository;
	
	@Before
	public void setUp() throws Exception {
		repository = new ExtensionRepository<MonitorMetrics>() {
			Set<MonitorMetrics> set = Sets.newHashSet();
			
			@Override
			public void add(MonitorMetrics monitorMetrics) {
				set.add(monitorMetrics);
			}
			
			@Override
			public Set<MonitorMetrics> set() {
				return set;
			}
		};
		
	}
	
	@Test
	public void testLoadMetricsFromSPI() throws Exception {
		ExtensionLoader loader = new ExtensionLoader();
		loader.loadMetricsFromSPI(repository, MonitorMetrics.class);
		Set<MonitorMetrics> set = repository.set();
		assertThat(set).hasSize(2);
	}
	
	@Test
	public void testLoadMetricsFromDefaultPackage() throws Exception {
		ExtensionLoader loader = new ExtensionLoader();
		loader.loadExtensionFromDefaultPackage(repository, MonitorMetrics.class);
		Set<MonitorMetrics> set = repository.set();
		assertThat(set).isNotEmpty();
	}
	
	@Test
	public void testGetScanPackage() throws Exception {
        ExtensionLoader loader = new ExtensionLoader();
        assertThat(loader.getScanPackage()).isEqualTo("com.yiji.framework.watcher");
	}
}
