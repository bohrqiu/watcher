package com.yiji.framework.watcher.metrics;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.yiji.framework.watcher.Constants;

/**
 * @author qiubo@yiji.com
 */
public class HealthCheckMetricsTest {
	private static final Logger logger = LoggerFactory.getLogger(HealthCheckMetricsTest.class);
	
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
		Map<String, Object> params = Maps.newHashMap();
		params.put(Constants.KEY, "ThreadDeadlock");
		Object result = healthCheckMetrics.monitor(params);
		logger.info("healthCheck result:{}", result);
		assertThat(result.toString()).contains("isHealthy");
	}
	
	@Test
	public void testMemoryStatusHealthCheck() throws Exception {
		Map<String, Object> params = Maps.newHashMap();
		params.put(Constants.KEY, "MemoryStatus");
		Object result = healthCheckMetrics.monitor(params);
        logger.info("healthCheck result:{}", result);
		assertThat(result.toString()).contains("isHealthy");
	}

    @Test
    public void testSystemLoadHealthCheck() throws Exception {
        Map<String, Object> params = Maps.newHashMap();
        params.put(Constants.KEY, "SystemLoad");
        Object result = healthCheckMetrics.monitor(params);
        logger.info("healthCheck result:{}", result);
        assertThat(result.toString()).contains("isHealthy");
    }
}