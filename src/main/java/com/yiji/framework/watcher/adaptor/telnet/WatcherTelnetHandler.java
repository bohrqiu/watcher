package com.yiji.framework.watcher.adaptor.telnet;

import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.telnet.TelnetHandler;
import com.alibaba.dubbo.remoting.telnet.support.Help;
import com.alibaba.dubbo.remoting.telnet.support.TelnetUtils;
import com.google.common.collect.Lists;
import com.yiji.framework.watcher.*;

/**
 * Dubbo的Telnet扩展。参见：http://dubbo.io/Developer+Guide-zh.htm#DeveloperGuide-zh-Telnet%E5%91%BD%E4%BB%A4%E6%89%A9%E5%B1%95
 * @author daidai@yiji.com
 */
@Help(parameter = "[-h] metricName [key1=val1,...]", summary = "show indicators provided by watcher",
		detail = "show watcher indicators. Usage: watch [-h] metricName key1=value1,key2=value2,...")
public class WatcherTelnetHandler implements TelnetHandler {
	private MonitorService monitorService = DefaultMonitorService.INSTANCE;
	private String helpInfo;
	
	public WatcherTelnetHandler() {
		String[] header = { "metricName", "description" };
		List<List<String>> table = Lists.newLinkedList();
		for (MonitorMetrics monitorMetrics : monitorService.monitorMetricses()) {
			List<String> row = Lists.newLinkedList();
			row.add(monitorMetrics.name());
			row.add(monitorMetrics.desc());
			table.add(row);
		}
		helpInfo = TelnetUtils.toTable(header, table);
	}
	
	@Override
	public String telnet(Channel channel, String message) throws RemotingException {
		if (message.equals("-h")) {
			return helpInfo;
		} else {
			try {
				MonitorRequest request = RequestParser.parse(message);
				request.setPrettyFormat(false);
				setResType(request.getParams(), request);
				if (request.getAction().equals("jstack")) {
					request.setResponseType(ResponseType.TEXT);
				}
				return monitorService.monitor(request);
			} catch (IllegalArgumentException e) {
				return "命令格式错误，正确的格式：metricName key1=value1,key2=value2,...";
			}
		}
	}
	
	private void setResType(Map<String, Object> paramMap, MonitorRequest request) {
		try {
			Object resType = paramMap.get("resType");
			if (resType == null) {
				request.setResponseType(ResponseType.JSON);
			} else {
				String str = resType.toString().toUpperCase();
				request.setResponseType(ResponseType.valueOf(str));
			}
		} catch (IllegalArgumentException e) {
			request.setResponseType(ResponseType.JSON);
		}
	}
	
}
