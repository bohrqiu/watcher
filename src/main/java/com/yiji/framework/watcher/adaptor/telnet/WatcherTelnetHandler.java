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
 * Created by SegeonTang on 2015/4/28.
 */
@Help(parameter = "[-h] metricName [key1=val1,...]", summary = "查看watcher提供的指标",
		detail = "查看watcher提供的指标，命令格式如下watch [-h] metricName key1=value1,key2=value2,...")
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
				if (ResponseType.valueOf(str) == null) {
					request.setResponseType(ResponseType.JSON);
				} else {
					request.setResponseType(ResponseType.valueOf(resType.toString()));
				}
			}
		} catch (IllegalArgumentException e) {
			request.setResponseType(ResponseType.JSON);
		}
	}
	
}
