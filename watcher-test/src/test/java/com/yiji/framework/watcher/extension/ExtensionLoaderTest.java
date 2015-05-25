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

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.yiji.framework.watcher.WatcherMetrics;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author qiubo@yiji.com
 */
public class ExtensionLoaderTest {
	private ExtensionRepository<WatcherMetrics> repository;
	
	@Before
	public void setUp() throws Exception {
		repository = new ExtensionRepository<WatcherMetrics>() {
			Set<WatcherMetrics> set = Sets.newHashSet();
			
			@Override
			public void add(WatcherMetrics watcherMetrics) {
				set.add(watcherMetrics);
			}
			
			@Override
			public Set<WatcherMetrics> set() {
				return set;
			}
		};
		
	}
	
	@Test
	public void testLoadMetricsFromSPI() throws Exception {
		ExtensionLoader loader = new ExtensionLoader();
		loader.loadMetricsFromSPI(repository, WatcherMetrics.class);
		Set<WatcherMetrics> set = repository.set();
		assertThat(set).hasSize(2);
	}
	
	@Test
	public void testLoadMetricsFromDefaultPackage() throws Exception {
		ExtensionLoader loader = new ExtensionLoader();
		loader.loadExtensionFromPackage(repository, WatcherMetrics.class, loader.getDefaultScanPackage());
		Set<WatcherMetrics> set = repository.set();
		assertThat(set).isNotEmpty();
	}
	
	@Test
	public void testGetScanPackage() throws Exception {
        ExtensionLoader loader = new ExtensionLoader();
        assertThat(loader.getDefaultScanPackage()).isEqualTo("com.yiji.framework.watcher");
	}

    @Test
    public void testGetCustomScanPackage() throws Exception {
        ExtensionLoader loader = new ExtensionLoader();
        assertThat(loader.getCustomScanPackage()).isNotEmpty();
    }
}
