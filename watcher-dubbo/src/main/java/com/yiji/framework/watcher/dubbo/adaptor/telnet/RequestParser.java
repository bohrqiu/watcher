package com.yiji.framework.watcher.dubbo.adaptor.telnet;

import java.util.regex.Pattern;

import com.yiji.framework.watcher.MonitorRequest;
import com.yiji.framework.watcher.Utils;

/**
 *
 * @author daidai@yiji.com
 */
public class RequestParser {
	public static final String KEY_VALUE_DELIM = "=";
	public static final char PARAM_SEPARATOR = ',';
	public static final String PRETTY_FORMAT_KEY = "prettyFormat";
	private static final String BLANK_CHAR_SEQ = "\\s+";
	private static final Pattern BLANK_CHAR_PATTERN = Pattern.compile(BLANK_CHAR_SEQ);
	
	/**
	 * 解析telnet命令行。命令行的格式如下： metricName key1=value1,key2=value2,...
	 * @param commandLine
	 * @return 解析出来的MonitorRequest对象
	 * @throws IllegalArgumentException
	 */
	public static MonitorRequest parse(String commandLine) throws IllegalArgumentException {
		String processed = Utils.trim(commandLine);
		if (Utils.isEmpty(processed))
			throw new IllegalArgumentException("命令为空");
		String[] parts = BLANK_CHAR_PATTERN.split(processed);
		String cmd = null;
		String params = null;
		if (parts != null) {
			if (parts.length <= 0) {
				// should never reach here
				throw new IllegalArgumentException("应该至少有一组匹配");
			} else if (parts.length == 1) {
				cmd = parts[0].trim();
			} else {
				cmd = parts[0].trim();
				params = parts[1].trim();
			}
		} else {
			// should never reach here
			throw new IllegalArgumentException("应该至少有一组匹配");
		}
		if (!Utils.isEmpty(cmd)) {
			MonitorRequest request = new MonitorRequest();
			request.setAction(cmd);
			setParams(params, request);
			return request;
		}
		return null;
	}
	
	private static void setParams(String params, MonitorRequest request) {
		if (!Utils.isEmpty(params)) {
			String[] paramSecs = Utils.split(params, PARAM_SEPARATOR);
			for (String param : paramSecs) {
				param = param.trim();
				if (!Utils.isEmpty(param)) {
					String[] pair = param.split(KEY_VALUE_DELIM);
					if (pair.length >= 2) {
						String key = pair[0].trim();
						String value = pair[1].trim();
						if (key.equals(PRETTY_FORMAT_KEY))
							request.getParams().put(key, value);
					}
				}
			}
		}
	}
}
