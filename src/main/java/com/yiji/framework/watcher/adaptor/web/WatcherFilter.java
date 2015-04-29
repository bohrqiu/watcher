/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-04-22 15:39 创建
 *
 */
package com.yiji.framework.watcher.adaptor.web;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;

/**
 * @author daidai@yiji.com
 */

public class WatcherFilter implements Filter {
	private static final String ALLOWABLE_IP_REGEX = "(127[.]0[.]0[.]1)|" + "(localhost)|" + "(10[.]\\d{1,3}[.]\\d{1,3}[.]\\d{1,3})|"
														+ "(172[.]((1[6-9])|(2\\d)|(3[01]))[.]\\d{1,3}[.]\\d{1,3})|"
														+ "(192[.]168[.]\\d{1,3}[.]\\d{1,3})";
	private static final Pattern pattern = Pattern.compile(ALLOWABLE_IP_REGEX);
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			String host = httpServletRequest.getRemoteHost();
			Matcher matcher = pattern.matcher(host);
			if (matcher.find()) {
				chain.doFilter(request, response);
			} else {
				logger.info("host: {}， 无权访问监控页！", host);
				response.getWriter().write("没有访问权限！");
			}
		} else {
			chain.doFilter(request, response);
		}
	}
	
	@Override
	public void destroy() {
		
	}
}
