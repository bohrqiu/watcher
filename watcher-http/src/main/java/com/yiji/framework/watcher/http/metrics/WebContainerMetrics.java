/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-27 11:25 创建
 *
 */
package com.yiji.framework.watcher.http.metrics;

import java.util.List;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import com.yiji.framework.watcher.metrics.base.AbstractCachedMonitorMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * @author qiubo@yiji.com
 */
public class WebContainerMetrics extends AbstractCachedMonitorMetrics {
	private static final Logger logger = LoggerFactory.getLogger(WebContainerMetrics.class);
	private static final String tomcatEmbedDomain = "Tomcat";
	private static final String tomcatDomain = "Catalina";
	
	public Object doMonitor(Map<String, Object> params) {
		Map<String, Object> map = Maps.newHashMap();
		
		List<MBeanServer> mBeanServers = MBeanServerFactory.findMBeanServer(null);
		if (!mBeanServers.isEmpty()) {
			MBeanServer mBeanServer = mBeanServers.get(0);
			String domain = getTomcatDomian(mBeanServer);
			try {
				
				ObjectName[] objNames = (ObjectName[]) mBeanServer.getAttribute(new ObjectName(domain, "type", "Service"), "connectorNames");
				for (ObjectName on : objNames) {
					Map<String, Object> connector = Maps.newHashMap();
					Object protocol = mBeanServer.getAttribute(on, "protocol");
					connector.put("protocol", protocol);
					Object acceptCount = mBeanServer.getAttribute(on, "acceptCount");
					connector.put("acceptCount", acceptCount);
					
					Object connectionTimeout = mBeanServer.getAttribute(on, "connectionTimeout");
					connector.put("connectionTimeout", connectionTimeout);
					
					Object protocolHandlerClassName = mBeanServer.getAttribute(on, "protocolHandlerClassName");
					connector.put("protocolHandlerClassName", protocolHandlerClassName);
					
					Object enableLookups = mBeanServer.getAttribute(on, "enableLookups");
					connector.put("enableLookups", enableLookups);
					
					Object uriEncoding = mBeanServer.getAttribute(on, "URIEncoding");
					connector.put("URIEncoding", uriEncoding);
					
					Object useBodyEncodingForURI = mBeanServer.getAttribute(on, "useBodyEncodingForURI");
					connector.put("useBodyEncodingForURI", useBodyEncodingForURI);
					
					Object localPort = mBeanServer.getAttribute(on, "localPort");
					map.put("connector-" + localPort, connector);
					
					if (protocolHandlerClassName.toString().equals("org.apache.coyote.http11.Http11NioProtocol")) {
						String threadPoolONStr = domain + ":type=ThreadPool,name=\"http-nio-" + localPort + "\"";
						ObjectName threadPoolON = new ObjectName(threadPoolONStr);
						Map<String, Object> threadPoolMap = Maps.newHashMap();
						
						Object connectionCount = mBeanServer.getAttribute(threadPoolON, "connectionCount");
						threadPoolMap.put("connectionCount", connectionCount);
						
						Object currentThreadCount = mBeanServer.getAttribute(threadPoolON, "currentThreadCount");
						threadPoolMap.put("currentThreadCount", currentThreadCount);
						
						Object currentThreadsBusy = mBeanServer.getAttribute(threadPoolON, "currentThreadsBusy");
						threadPoolMap.put("currentThreadsBusy", currentThreadsBusy);
						
						Object keepAliveCount = mBeanServer.getAttribute(threadPoolON, "keepAliveCount");
						threadPoolMap.put("keepAliveCount", keepAliveCount);
						
						Object maxConnections = mBeanServer.getAttribute(threadPoolON, "maxConnections");
						threadPoolMap.put("maxConnections", maxConnections);
						
						Object maxThreads = mBeanServer.getAttribute(threadPoolON, "maxThreads");
						threadPoolMap.put("maxThreads", maxThreads);
						
						Object minSpareThreads = mBeanServer.getAttribute(threadPoolON, "minSpareThreads");
						threadPoolMap.put("minSpareThreads", minSpareThreads);
						connector.put("threadPool", threadPoolMap);
					}
				}
			} catch (Exception e) {
				logger.warn("获取信息失败", e);
				map.put("errorMsg", e.getMessage());
			}
		}
		return map;
	}
	
	@Override
	public CacheTime getCacheTime() {
		return CacheTime.THIRTY_SECOND;
	}
	
	private String getTomcatDomian(MBeanServer mBeanServer) {
		try {
			mBeanServer.getAttribute(new ObjectName(tomcatEmbedDomain, "type", "Service"), "connectorNames");
			return tomcatEmbedDomain;
		} catch (Exception e) {
			return tomcatDomain;
		}
	}
	
	public String name() {
		return "webContainer";
	}
	
	public String desc() {
		return "web container info";
	}
}
