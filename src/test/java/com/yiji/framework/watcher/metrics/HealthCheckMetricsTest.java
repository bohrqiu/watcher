package com.yiji.framework.watcher.metrics;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author qiubo@yiji.com
 */
public class HealthCheckMetricsTest {
	private static HealthCheckMetrics healthCheckMetrics;
	
	@BeforeClass
	public static void setUp() throws Exception {
		healthCheckMetrics = new HealthCheckMetrics();
	}
	
	@Test
	public void testLoadExtensions() throws Exception {
		assertThat(healthCheckMetrics.getHealthCheckNames()).isNotEmpty();
	}
	
	@Test
	public void testThreadDeadlockHealthCheck() throws Exception {
		Object result = healthCheckMetrics.monitor(new HashMap<String, Object>());
		assertThat(result.toString()).contains("ThreadDeadlockHealthCheck");
	}
}