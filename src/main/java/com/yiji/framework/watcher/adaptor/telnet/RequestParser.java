package com.yiji.framework.watcher.adaptor.telnet;

import java.util.regex.Pattern;

import com.yiji.framework.watcher.MonitorRequest;
import com.yjf.common.util.StringUtils;

/**
 *
 * @author daidai@yiji.com
 */
public class RequestParser {
    public static final String KEY_VALUE_DELIM = "=";
    public static final String PARAM_SEPARATOR = ",";
    public static final String PRETTY_FORMAT_KEY = "prettyFormat";
    private static final String BLANK_CHAR_SEQ = "\\s+";
    private static final Pattern BLANK_CHAR_PATTERN = Pattern.compile(BLANK_CHAR_SEQ);

    /**
     * 解析命令行。命令行的格式如下：
     * metricName key1=value1,key2=value2,...
     * @param commandLine
     * @return
     * @throws IllegalArgumentException
     */
    public static MonitorRequest parse(String commandLine) throws IllegalArgumentException{
        String processed = StringUtils.trim(commandLine);
        if (StringUtils.isEmpty(processed))
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
        if (!StringUtils.isEmpty(cmd)) {
            MonitorRequest request = new MonitorRequest();
            request.setAction(cmd);
            if (!StringUtils.isEmpty(params)) {
                String[] paramSecs = StringUtils.split(params, PARAM_SEPARATOR);
                for (String param : paramSecs) {
                    param = param.trim();
                    if (!StringUtils.isEmpty(param)) {
                        String[] pair = param.split(KEY_VALUE_DELIM);
                        if (pair != null && pair.length >= 2) {
                            String key = pair[0].trim();
                            String value = pair[1].trim();
                            if (key.equals(PRETTY_FORMAT_KEY))
                            request.getParams().put(pair[0], pair[1]);
                        }
                    }
                }
            }
            return request;
        }
        return null;
    }
}
