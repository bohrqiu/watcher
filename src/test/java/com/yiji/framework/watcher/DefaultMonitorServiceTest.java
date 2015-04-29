package com.yiji.framework.watcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Map;

import org.junit.Test;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.JSONObject;
import com.yiji.framework.watcher.metrics.AbstractMonitorMetrics;

/**
 * @author qzhanbo@yiji.com
 */
public class DefaultMonitorServiceTest {
	
	MonitorService monitorService = DefaultMonitorService.INSTANCE;
	
	@Test
	public void testNetinfo() throws Exception {
		if (isNotWindows()) {
			MonitorRequest monitorRequest = new MonitorRequest();
			monitorRequest.setAction("netinfo");
			String result = monitorService.monitor(monitorRequest);
			JSONObject object = (JSONObject) JSON.parse(result);
			assertThat(object.get("PrimaryDns")).isNotNull();
		}

	}
	
	@Test
	public void testNetStat() throws Exception {
		if (isNotWindows()) {
			MonitorRequest monitorRequest = new MonitorRequest();
			monitorRequest.setAction("netstat");
			String result = monitorService.monitor(monitorRequest);
			JSONObject object = (JSONObject) JSON.parse(result);
			assertThat(object.get("tcpInboundTotal")).isNotNull();
		}
	}
	
	@Test
	public void testOSCPU() throws Exception {
		if (isNotWindows()) {
			MonitorRequest monitorRequest = new MonitorRequest();
			monitorRequest.setAction("swap");
			String result = monitorService.monitor(monitorRequest);
			JSONObject object = (JSONObject) JSON.parse(result);
			assertThat(object.get("Used")).isNotNull();
		}
	}
	
	@Test
	public void testNotExists() throws Exception {
		String action = "xxx";
		MonitorRequest monitorRequest = new MonitorRequest();
		monitorRequest.setAction(action);
		monitorRequest.responseJson().prettyFormat();
		String result = monitorService.monitor(monitorRequest);
		JSONObject object = (JSONObject) JSON.parse(result);
		assertThat(object.get("msg")).isEqualTo("unsupport monitor metrics:" + action);
	}
	
	@Test
	public void testAddMonitorMetrics() throws Exception {
		
		try {
			MonitorMetrics monitorMetrics = new AbstractMonitorMetrics() {
				@Override
				public Object monitor(Map<String, Object> params) {
					return null;
				}
				
				@Override
				public String name() {
					return "gc";
				}
				
				@Override
				public String desc() {
					return "test";
				}
			};
			monitorService.addMonitorMetrics(monitorMetrics);
			fail("应该抛出监控指标已存在的异常");
		} catch (Exception e) {
			assertThat(e).hasMessageContaining("gc监控指标已经存在");
		}
		
	}
	
	@Test
	public void testWebContainer() throws Exception {
		String action = "webContainer";
		MonitorRequest monitorRequest = new MonitorRequest();
		monitorRequest.setAction(action);
		monitorRequest.responseJson().prettyFormat();
		String result = monitorService.monitor(monitorRequest);
		assertThat(result).isEqualTo("{}");
	}
	
	public static boolean isNotWindows() {
		return !System.getProperty("os.name").startsWith("Windows");
	}
}