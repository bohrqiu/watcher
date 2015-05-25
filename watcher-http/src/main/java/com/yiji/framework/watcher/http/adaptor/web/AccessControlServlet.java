/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-30 11:47 创建
 *
 */
package com.yiji.framework.watcher.http.adaptor.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.yiji.framework.watcher.Constants;
import com.yiji.framework.watcher.Utils;
import com.yiji.framework.watcher.http.adaptor.web.util.IPAddress;
import com.yiji.framework.watcher.http.adaptor.web.util.IPRange;

/**
 * @author qiubo@yiji.com
 */
public class AccessControlServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(AccessControlServlet.class);
	public static final String PARAM_NAME_ALLOW = "allow";
	public static final String PARAM_NAME_DENY = "deny";
	public static final String PARAM_REMOTE_ADDR = "clientIpHttpHeaderName";
	public static final String PARAM_NAME_DENY_ALL = "denyAll";
	public static final String UNKNOWN = "unknown";
	/**
	 * 用户ip header
	 * name，如果不指定此值，会优先从x-forwarded-for、WL-Proxy-Client-IP中获取ip，如果仍然获取不到，获取连接ip
	 */
	protected String clientIpHttpHeaderName;
	
	/**
	 * 是否允许内网访问，默认启用
	 */
	protected boolean allowIntranetAccess = true;
	
	/**
	 * 允许ip列表
	 */
	protected List<IPRange> allowList = new ArrayList<IPRange>();
	protected List<IPRange> denyList = new ArrayList<IPRange>();
	/**
	 * 是否禁止所有的请求访问
	 */
	private boolean denyAll = false;
	/**
	 * 内网ip地址范围
	 */
	private List<IPRange> intranetIpRange = Lists
		.newArrayList(new IPRange("10.0.0.0/8"), new IPRange("172.16.0.0/12"), new IPRange("192.168.0.0/16"));
	
	public AccessControlServlet() {
	}
	
	@Override
	public void init() throws ServletException {
		String paramdenyAll = getInitParameter(PARAM_NAME_DENY_ALL);
		this.denyAll = Boolean.parseBoolean(paramdenyAll);
		
		String paramRemoteAddressHeader = getInitParameter(PARAM_REMOTE_ADDR);
		if (!Utils.isEmpty(paramRemoteAddressHeader)) {
			this.clientIpHttpHeaderName = paramRemoteAddressHeader;
		}
		try {
			String param = getInitParameter(PARAM_NAME_ALLOW);
			if (param != null && param.trim().length() != 0) {
				param = param.trim();
				String[] items = param.split(",");
				
				for (String item : items) {
					if (item == null || item.length() == 0) {
						continue;
					}
					
					IPRange ipRange = new IPRange(item);
					allowList.add(ipRange);
				}
			}
		} catch (Exception e) {
			logger.error("initParameter config error, allow : " + getInitParameter(PARAM_NAME_ALLOW), e);
		}
		
		try {
			String param = getInitParameter(PARAM_NAME_DENY);
			if (param != null && param.trim().length() != 0) {
				param = param.trim();
				String[] items = param.split(",");
				
				for (String item : items) {
					if (item == null || item.length() == 0) {
						continue;
					}
					
					IPRange ipRange = new IPRange(item);
					denyList.add(ipRange);
				}
			}
			
		} catch (Exception e) {
			logger.error("initParameter config error, deny : " + getInitParameter(PARAM_NAME_DENY), e);
		}
		setScanPackage(getInitParameter(Constants.WATCHER_SCAN_PACKAGE));
	}
	
	public void setScanPackage(String scanPackage) {
		if (!Strings.isNullOrEmpty(scanPackage)) {
			System.setProperty(Constants.WATCHER_SCAN_PACKAGE, scanPackage);
		}
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding(Charsets.UTF_8.name());
		resp.setCharacterEncoding(Charsets.UTF_8.name());
		String ip = getIpAddr(req);
		if (!isRequestAllow(ip)) {
			logger.info("clientIp={},访问watcher页面被拒绝", ip);
			resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
			resp.getWriter().write("Forbidden");
			return;
		}
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setHeader("Cache-Control", "must-revalidate,no-cache,no-store");
		super.service(req, resp);
	}
	
	public boolean isRequestAllow(String remoteAddress) {
		if (denyAll) {
			return false;
		}
		boolean ipV6 = remoteAddress != null && remoteAddress.indexOf(':') != -1;
		
		if (ipV6) {
			if ("0:0:0:0:0:0:0:1".equals(remoteAddress)) {
				return true;
			}
			
			if (denyList.size() == 0 && allowList.size() == 0) {
				return true;
			}
			
			return false;
		}
		
		if ("127.0.0.1".equals(remoteAddress)) {
			return true;
		}
		
		IPAddress ipAddress;
		try {
			ipAddress = new IPAddress(remoteAddress);
		} catch (Exception e) {
			logger.debug("ip解析错误", e);
			return false;
		}
		
		if (allowIntranetAccess) {
			for (IPRange range : intranetIpRange) {
				if (range.isIPAddressInRange(ipAddress)) {
					return true;
				}
			}
		}
		
		for (IPRange range : denyList) {
			if (range.isIPAddressInRange(ipAddress)) {
				return false;
			}
		}
		
		if (allowList.size() > 0) {
			for (IPRange range : allowList) {
				if (range.isIPAddressInRange(ipAddress)) {
					return true;
				}
			}
			return false;
		}
		
		return true;
	}
	
	public String getIpAddr(HttpServletRequest request) {
		if (request == null) {
			return UNKNOWN;
		} else {
			if (clientIpHttpHeaderName != null) {
				String clientIp = request.getHeader("clientIpHttpHeaderName");
				if (clientIp != null) {
					return clientIp;
				}
			}
			String ip = request.getHeader("x-forwarded-for");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			
			return ip;
		}
	}
	
	public List<IPRange> getDenyList() {
		return denyList;
	}
	
	public void setDenyList(List<IPRange> denyList) {
		this.denyList = denyList;
	}
	
	public String getClientIpHttpHeaderName() {
		return clientIpHttpHeaderName;
	}
	
	public void setClientIpHttpHeaderName(String clientIpHttpHeaderName) {
		this.clientIpHttpHeaderName = clientIpHttpHeaderName;
	}
	
	public List<IPRange> getAllowList() {
		return allowList;
	}
	
	public void setAllowList(List<IPRange> allowList) {
		this.allowList = allowList;
	}
	
	public boolean isAllowIntranetAccess() {
		return allowIntranetAccess;
	}
	
	public void setAllowIntranetAccess(boolean allowIntranetAccess) {
		this.allowIntranetAccess = allowIntranetAccess;
	}
	
	public boolean isDenyAll() {
		return denyAll;
	}
	
	public void setDenyAll(boolean denyAll) {
		this.denyAll = denyAll;
	}
}
