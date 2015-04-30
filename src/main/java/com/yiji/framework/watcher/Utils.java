/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-29 11:21 创建
 *
 */
package com.yiji.framework.watcher;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiubo@yiji.com
 */
public class Utils {
	
	public static final String[] EMPTY_STRINGS = {};
	
	private static Long pid = null;
	
	public static String trim(String str) {
		if (str == null) {
			return null;
		} else {
			return str.trim();
		}
	}
	
	public static boolean isEmpty(String str) {
		return ((str == null) || (str.length() == 0));
	}
	
	public static String[] split(String str, char separatorChar) {
		if (str == null) {
			return null;
		}
		int length = str.length();
		
		if (length == 0) {
			return EMPTY_STRINGS;
		}
		
		List list = new ArrayList();
		int i = 0;
		int start = 0;
		boolean match = false;
		
		while (i < length) {
			if (str.charAt(i) == separatorChar) {
				if (match) {
					list.add(str.substring(start, i));
					match = false;
				}
				start = ++i;
				continue;
			}
			match = true;
			i++;
		}
		if (match) {
			list.add(str.substring(start, i));
		}
		return (String[]) list.toArray(new String[list.size()]);
	}
	
	public static Long getPid() {
		if (pid == null) {
			final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
			final int index = jvmName.indexOf('@');
			
			if (index < 1) {
				return -1l;
			}
			try {
				pid = Long.parseLong(jvmName.substring(0, index));
				return pid;
			} catch (NumberFormatException e) {
				// ignore
				return -1l;
			}
		} else {
			return pid;
		}
	}
}
